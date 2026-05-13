package co.edu.udistrital.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage Database connections using MySQL Connector.
 */
public class DatabaseConnection {
    
    // Cambiamos mariadb por mysql y añadimos parámetros de zona horaria por estabilidad
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_triage_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root"; 
    private static final String PASSWORD = "Ing86gb.."; 

    public static Connection getConnection() throws SQLException {
        try {
            // Se actualiza el motor al de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found in classpath.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}