package co.edu.udistrital.data;

import co.edu.udistrital.config.DatabaseConnection;
import co.edu.udistrital.model.MedicalTurn;
import co.edu.udistrital.model.Patient;

import java.sql.*;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Data Access Object (DAO) / Repositorio.
 * Cumple con el Principio de Responsabilidad Única (SRP) de SOLID: 
 * Su única tarea es gestionar la persistencia de datos (MySQL y RAM).
 */
public class TurnRepository {
    
    private static TurnRepository instance;
    private final PriorityQueue<MedicalTurn> turnQueue;
    private final Stack<MedicalTurn> attentionHistory;

    /**
     * Constructor privado para evitar instanciación externa.
     * Inicializa las estructuras e hidrata la cola en RAM con los datos de MySQL.
     */
    private TurnRepository() {
        this.turnQueue = new PriorityQueue<>();
        this.attentionHistory = new Stack<>();
        loadPendingTurns(); 
    }

    /**
     * Garantiza que exista una única instancia del Repositorio en la memoria del servidor Tomcat.
     * Usa la palabra 'synchronized' para evitar colisiones si dos usuarios entran al mismo milisegundo.
     *
     * @return La instancia única de TurnRepository.
     */
    public static synchronized TurnRepository getInstance() {
        if (instance == null) {
            instance = new TurnRepository();
        }
        return instance;
    }

    /**
     * Guarda el paciente y su turno en la base de datos y posteriormente en la memoria.
     *
     * @param turn El objeto MedicalTurn configurado con los datos ingresados.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean saveTurn(MedicalTurn turn) {
        String sqlPatient = "INSERT INTO patients (document_number, full_name, age) VALUES (?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE full_name = VALUES(full_name), age = VALUES(age)";
        String sqlTurn = "INSERT INTO medical_turns (patient_id, triage_level, symptoms, arrival_time, status) VALUES (?, ?, ?, ?, 'WAITING')";

        try (Connection conn = DatabaseConnection.getConnection()) {
            int patientId = -1;
            
            // 1. Inserción o actualización del paciente
            try (PreparedStatement pstmt = conn.prepareStatement(sqlPatient, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, turn.getPatient().getDocumentNumber());
                pstmt.setString(2, turn.getPatient().getFullName());
                pstmt.setInt(3, turn.getPatient().getAge());
                pstmt.executeUpdate();
                
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) patientId = rs.getInt(1);
                }
            }
            
            // Recupera el ID si el paciente ya existía en el sistema
            if (patientId == -1) {
                try (PreparedStatement idStmt = conn.prepareStatement("SELECT id FROM patients WHERE document_number = ?")) {
                    idStmt.setString(1, turn.getPatient().getDocumentNumber());
                    try (ResultSet rs = idStmt.executeQuery()) {
                        if (rs.next()) patientId = rs.getInt("id");
                    }
                }
            }

            // 2. Inserción del Turno Médico asociado al Paciente
            try (PreparedStatement tstmt = conn.prepareStatement(sqlTurn, Statement.RETURN_GENERATED_KEYS)) {
                tstmt.setInt(1, patientId);
                tstmt.setInt(2, turn.getTriageLevel());
                tstmt.setString(3, turn.getSymptoms());
                tstmt.setTimestamp(4, turn.getArrivalTime());
                tstmt.executeUpdate();
                
                try (ResultSet rs = tstmt.getGeneratedKeys()) {
                    if (rs.next()) turn.setId(rs.getInt(1)); // Se actualiza la entidad en memoria con el ID real
                }
            }
            
            // 3. Se inserta en la estructura de datos dinámica
            turnQueue.offer(turn);
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza el estado del turno en la base de datos a 'ATENDIDO'.
     *
     * @param turn El turno extraído de la cola para ser actualizado.
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

    /**
     * Método interno para cargar los turnos pendientes desde MySQL a la RAM 
     * en caso de que el servidor Tomcat sea reiniciado, previniendo pérdida de la cola.
     */
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