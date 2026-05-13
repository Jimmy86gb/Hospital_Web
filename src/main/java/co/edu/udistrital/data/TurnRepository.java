package co.edu.udistrital.data;

import co.edu.udistrital.config.DatabaseConnection;
import co.edu.udistrital.model.MedicalTurn;
import co.edu.udistrital.model.Patient;

import java.sql.*;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Data Access Object (DAO) / Repository.
 * Sole responsibility (SRP): Manage data persistence in MySQL and memory structures.
 */
public class TurnRepository {
    
    private static TurnRepository instance;
    private final PriorityQueue<MedicalTurn> turnQueue;
    private final Stack<MedicalTurn> attentionHistory;

    private TurnRepository() {
        this.turnQueue = new PriorityQueue<>();
        this.attentionHistory = new Stack<>();
        loadPendingTurns(); 
    }

    /**
     * Ensures only one instance of the Repository exists in Tomcat memory.
     *
     * @return TurnRepository instance.
     */
    public static synchronized TurnRepository getInstance() {
        if (instance == null) {
            instance = new TurnRepository();
        }
        return instance;
    }

    /**
     * Saves a patient and their turn into the database.
     *
     * @param turn MedicalTurn object containing patient details.
     * @return true if successful, false otherwise.
     */
    public boolean saveTurn(MedicalTurn turn) {
        String sqlPatient = "INSERT INTO patients (document_number, full_name, age) VALUES (?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE full_name = VALUES(full_name), age = VALUES(age)";
        String sqlTurn = "INSERT INTO medical_turns (patient_id, triage_level, symptoms, arrival_time, status) VALUES (?, ?, ?, ?, 'WAITING')";

        try (Connection conn = DatabaseConnection.getConnection()) {
            int patientId = -1;
            
            // Insert or Update Patient
            try (PreparedStatement pstmt = conn.prepareStatement(sqlPatient, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, turn.getPatient().getDocumentNumber());
                pstmt.setString(2, turn.getPatient().getFullName());
                pstmt.setInt(3, turn.getPatient().getAge());
                pstmt.executeUpdate();
                
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) patientId = rs.getInt(1);
                }
            }
            
            // Retrieve ID if patient already existed
            if (patientId == -1) {
                try (PreparedStatement idStmt = conn.prepareStatement("SELECT id FROM patients WHERE document_number = ?")) {
                    idStmt.setString(1, turn.getPatient().getDocumentNumber());
                    try (ResultSet rs = idStmt.executeQuery()) {
                        if (rs.next()) patientId = rs.getInt("id");
                    }
                }
            }

            // Insert Turn
            try (PreparedStatement tstmt = conn.prepareStatement(sqlTurn, Statement.RETURN_GENERATED_KEYS)) {
                tstmt.setInt(1, patientId);
                tstmt.setInt(2, turn.getTriageLevel());
                tstmt.setString(3, turn.getSymptoms());
                tstmt.setTimestamp(4, turn.getArrivalTime());
                tstmt.executeUpdate();
                
                try (ResultSet rs = tstmt.getGeneratedKeys()) {
                    if (rs.next()) turn.setId(rs.getInt(1));
                }
            }
            
            // Update RAM Structure
            turnQueue.offer(turn);
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates turn status in DB and memory.
     *
     * @param turn The turn to mark as attended.
     */
    public void updateTurnStatus(MedicalTurn turn) {
        String sql = "UPDATE medical_turns SET status = 'ATTENDED' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, turn.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPendingTurns() {
        String sql = "SELECT t.id, t.triage_level, t.symptoms, t.arrival_time, " +
                     "p.id as p_id, p.document_number, p.full_name, p.age " +
                     "FROM medical_turns t JOIN patients p ON t.patient_id = p.id WHERE t.status = 'WAITING'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                Patient p = new Patient(rs.getInt("p_id"), rs.getString("document_number"), rs.getString("full_name"), rs.getInt("age"));
                MedicalTurn turn = new MedicalTurn(rs.getInt("id"), p, rs.getInt("triage_level"), rs.getString("symptoms"), rs.getTimestamp("arrival_time"));
                turnQueue.offer(turn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PriorityQueue<MedicalTurn> getQueue() { return turnQueue; }
    public Stack<MedicalTurn> getHistory() { return attentionHistory; }
}