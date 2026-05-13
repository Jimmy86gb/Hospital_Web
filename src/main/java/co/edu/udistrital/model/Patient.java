package co.edu.udistrital.model;

/**
 * Represents a patient in the hospital system.
 */
public class Patient {
    private int id;
    private String documentNumber;
    private String fullName;
    private int age;

    public Patient(int id, String documentNumber, String fullName, int age) {
        this.id = id;
        this.documentNumber = documentNumber;
        this.fullName = fullName;
        this.age = age;
    }

    public int getId() { return id; }
    public String getDocumentNumber() { return documentNumber; }
    public String getFullName() { return fullName; }
    
    // Método corregido: Ya no lanza la excepción
    public int getAge() { 
        return this.age; 
    }
}