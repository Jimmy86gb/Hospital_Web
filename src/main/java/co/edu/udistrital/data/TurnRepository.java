package co.edu.udistrital.data;

import co.edu.udistrital.config.DatabaseConnection;
import co.edu.udistrital.model.MedicalTurn;
import co.edu.udistrital.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Repository handling both Database persistence (MariaDB) and Memory structures.
 * Uses Singleton pattern.
 */
public class TurnRepository {
    
    private static TurnRepository instance;
    private PriorityQueue<MedicalTurn> turnQueue;
    private Stack<MedicalTurn> attentionHistory;

    private TurnRepository() {
        this.turnQueue = new PriorityQueue<>();
        this.attentionHistory = new Stack<>();
        loadWaitingTurnsFromDB(); // Initialize RAM with DB state on startup
    }

    public static synchronized TurnRepository getInstance() {
        if (instance == null) {
            instance = new TurnRepository();
        }
        return instance;
    }

    /**
     * Inserts patient and turn into MariaDB, then adds to memory Queue.
     */
    public void registerTurn(MedicalTurn turn) {
        String insertPatientSql = "INSERT INTO patients (document_number, full_name, age) VALUES (?, ?, ?) " +
                                  "ON DUPLICATE KEY UPDATE full_name = VALUES(full_name), age = VALUES(age)";
        
        String insertTurnSql = "INSERT INTO medical_turns (patient_id, triage_level, symptoms, arrival_time, status) " +
                               "VALUES (?, ?, ?, ?, 'WAITING')";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1. Handle Patient
            int patientId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(insertPatientSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, turn.getPatient().getDocumentNumber());
                pstmt.setString(2, turn.getPatient().getFullName());
                pstmt.setInt(3, turn.getPatient().getAge());
                pstmt.executeUpdate();
                
                // Get the generated or existing patient ID
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        patientId = rs.getInt(1);
                    }
                }
            }
            
            // Fallback if patient already existed and ID wasn't generated
            if (patientId == -1) {
                String getIdSql = "SELECT id FROM patients WHERE document_number = ?";
                try (PreparedStatement idStmt = conn.prepareStatement(getIdSql)) {
                    idStmt.setString(1, turn.getPatient().getDocumentNumber());
                    try (ResultSet rs = idStmt.executeQuery()) {
                        if (rs.next()) patientId = rs.getInt("id");
                    }
                }
            }

            // 2. Handle Turn
            try (PreparedStatement tstmt = conn.prepareStatement(insertTurnSql, Statement.RETURN_GENERATED_KEYS)) {
                tstmt.setInt(1, patientId);
                tstmt.setInt(2, turn.getTriageLevel());
                tstmt.setString(3, turn.getSymptoms());
                tstmt.setTimestamp(4, turn.getArrivalTime());
                tstmt.executeUpdate();
                
                try (ResultSet rs = tstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        turn.setId(rs.getInt(1)); // Update memory object with real DB ID
                    }
                }
            }
            
            // 3. Add to RAM structure
            turnQueue.offer(turn);
            
        } catch (SQLException e) {
            e.printStackTrace(); // In a real app, use a Logger
        }
    }

    /**
     * Retrieves highest priority turn, updates MariaDB to ATTENDED, pushes to Stack.
     */
    public MedicalTurn getNextPatient() {
        MedicalTurn nextTurn = turnQueue.poll();
        
        if (nextTurn != null) {
            String updateSql = "UPDATE medical_turns SET status = 'ATTENDED' WHERE id = ?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                 
                pstmt.setInt(1, nextTurn.getId());
                pstmt.executeUpdate();
                
                nextTurn.setStatus("ATTENDED");
                attentionHistory.push(nextTurn);
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return nextTurn;
    }

    /**
     * Private helper to load pending turns into RAM if the Tomcat server restarts.
     */
    private void loadWaitingTurnsFromDB() {
        String sql = "SELECT t.id, t.triage_level, t.symptoms, t.arrival_time, " +
                     "p.id as p_id, p.document_number, p.full_name, p.age " +
                     "FROM medical_turns t " +
                     "JOIN patients p ON t.patient_id = p.id " +
                     "WHERE t.status = 'WAITING'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                Patient p = new Patient(
                    rs.getInt("p_id"), 
                    rs.getString("document_number"), 
                    rs.getString("full_name"), 
                    rs.getInt("age")
                );
                
                MedicalTurn turn = new MedicalTurn(
                    rs.getInt("id"), 
                    p, 
                    rs.getInt("triage_level"), 
                    rs.getString("symptoms"), 
                    rs.getTimestamp("arrival_time")
                );
                
                turnQueue.offer(turn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MedicalTurn getLastAttendedPatient() {
        if (!attentionHistory.isEmpty()) {
            return attentionHistory.peek();
        }
        return null;
    }

    public PriorityQueue<MedicalTurn> getTurnQueue() {
        return turnQueue;
    }
}