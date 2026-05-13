package co.edu.udistrital.model;

/**
 * Representa a un paciente dentro del sistema hospitalario.
 * Actúa como un Data Transfer Object (DTO) puro para almacenar información demográfica.
 */
public class Patient {
    private int id;
    private String documentNumber;
    private String fullName;
    private int age;

    /**
     * Construye una nueva instancia de Paciente.
     *
     * @param id             El identificador asignado por la base de datos.
     * @param documentNumber El número de identificación oficial (Cédula/Tarjeta).
     * @param fullName       El nombre completo del paciente.
     * @param age            La edad cronológica del paciente.
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