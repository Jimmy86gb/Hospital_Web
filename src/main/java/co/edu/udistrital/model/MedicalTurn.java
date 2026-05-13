package co.edu.udistrital.model;

import java.sql.Timestamp;

/**
 * Representa la asignación de un turno médico.
 * Implementa la interfaz Comparable para dictar la prioridad interna 
 * cuando se inserta en la estructura PriorityQueue de Java.
 */
public class MedicalTurn implements Comparable<MedicalTurn> {
    private int id;
    private Patient patient;
    private int triageLevel;
    private String symptoms;
    private Timestamp arrivalTime;
    private String status; 

    /**
     * Construye un nuevo Turno Médico.
     *
     * @param id          ID del turno en la base de datos.
     * @param patient     El objeto Paciente asociado a este turno.
     * @param triageLevel Nivel de urgencia (1 Crítico a 5 No Urgente).
     * @param symptoms    Descripción de las dolencias reportadas.
     * @param arrivalTime Marca de tiempo exacta del registro.
     */
    public MedicalTurn(int id, Patient patient, int triageLevel, String symptoms, Timestamp arrivalTime) {
        this.id = id;
        this.patient = patient;
        this.triageLevel = triageLevel;
        this.symptoms = symptoms;
        this.arrivalTime = arrivalTime;
        this.status = "WAITING"; // Estado por defecto
    }

    /**
     * Lógica central de priorización para la Cola de Prioridad.
     * Un nivel de triage numéricamente menor indica mayor gravedad médica.
     * En caso de empate en el nivel, el turno con fecha/hora de llegada más antigua gana.
     *
     * @param other El otro turno contra el que se compara.
     * @return un entero negativo, cero, o positivo según la prioridad calculada.
     */
    @Override
    public int compareTo(MedicalTurn other) {
        if (this.triageLevel != other.triageLevel) {
            return Integer.compare(this.triageLevel, other.triageLevel);
        }
        return this.arrivalTime.compareTo(other.arrivalTime);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public int getTriageLevel() { return triageLevel; }
    public String getSymptoms() { return symptoms; }
    public Timestamp getArrivalTime() { return arrivalTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}