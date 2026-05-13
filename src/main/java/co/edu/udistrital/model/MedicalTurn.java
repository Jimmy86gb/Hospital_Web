package co.edu.udistrital.model;

import java.sql.Timestamp;

/**
 * Represents a medical turn assignment.
 * Implements Comparable to dictate priority inside the PriorityQueue.
 */
public class MedicalTurn implements Comparable<MedicalTurn> {
    private int id;
    private Patient patient;
    private int triageLevel;
    private String symptoms;
    private Timestamp arrivalTime;
    private String status; 

    /**
     * Constructs a new Medical Turn.
     *
     * @param id          Database ID of the turn.
     * @param patient     The associated Patient object.
     * @param triageLevel The urgency level (1 Critical - 5 Non-Urgent).
     * @param symptoms    Description of the patient's ailments.
     * @param arrivalTime Timestamp of registry.
     */
    public MedicalTurn(int id, Patient patient, int triageLevel, String symptoms, Timestamp arrivalTime) {
        this.id = id;
        this.patient = patient;
        this.triageLevel = triageLevel;
        this.symptoms = symptoms;
        this.arrivalTime = arrivalTime;
        this.status = "WAITING";
    }

    /**
     * Core logic for the PriorityQueue.
     * Lower triage level number means higher priority.
     * If levels are identical, the earlier arrival time takes precedence.
     *
     * @param other The other turn to compare against.
     * @return negative integer, zero, or positive integer based on priority.
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