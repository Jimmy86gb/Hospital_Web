package co.edu.udistrital.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para gestionar las conexiones a la base de datos MySQL.
 * Aplica conceptos del patrón Singleton limitando la configuración a un solo punto.
 */
public class DatabaseConnection {
    
    // URL de conexión utilizando el conector MySQL para evitar conflictos en Windows
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_triage_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 

    /**
     * Establece y retorna una nueva conexión a la base de datos MySQL.
     *
     * @return Objeto Connection para ejecutar transacciones SQL.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("El Driver de MySQL no fue encontrado en el classpath.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}