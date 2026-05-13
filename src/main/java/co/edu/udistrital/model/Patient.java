package co.edu.udistrital.model;

/**
 * Represents a medical patient in the system.
 * This acts as a pure Data Transfer Object (DTO).
 */
public class Patient {
    private int id;
    private String documentNumber;
    private String fullName;
    private int age;

    /**
     * Constructs a new Patient instance.
     *
     * @param id             The database identifier.
     * @param documentNumber The official identification number.
     * @param fullName       The patient's complete name.
     * @param age            The patient's age.
     */
    public Patient(int id, String documentNumber, String fullName, int age) {
        this.id = id;
        this.documentNumber = documentNumber;
        this.fullName = fullName;
        this.age = age;
    }

    public int getId() { return id; }
    public String getDocumentNumber() { return documentNumber; }
    public String getFullName() { return fullName; }
    public int getAge() { return age; }
}