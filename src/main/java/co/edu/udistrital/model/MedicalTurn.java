package co.edu.udistrital.model;

import java.sql.Timestamp;

public class MedicalTurn implements Comparable<MedicalTurn> {
    private int id;
    private Patient patient;
    private int triageLevel;
    private String symptoms;
    private Timestamp arrivalTime;
    private String status;

    public MedicalTurn(int id, Patient patient, int triageLevel, String symptoms, Timestamp arrivalTime) {
        this.id = id;
        this.patient = patient;
        this.triageLevel = triageLevel;
        this.symptoms = symptoms;
        this.arrivalTime = arrivalTime;
        this.status = "WAITING";
    }

    @Override
    public int compareTo(MedicalTurn other) {
        if (this.triageLevel != other.triageLevel) {
            return Integer.compare(this.triageLevel, other.triageLevel);
        }
        return this.arrivalTime.compareTo(other.arrivalTime);
    }

    public int getId() { return id; }
    public Patient getPatient() { return patient; }
    public int getTriageLevel() { return triageLevel; }
    public Timestamp getArrivalTime() { return arrivalTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Métodos corregidos: Devuelven y asignan el valor real
    public String getSymptoms() {
        return this.symptoms;
    }

    public void setId(int id) {
        this.id = id;
    }
}