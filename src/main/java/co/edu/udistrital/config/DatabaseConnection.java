package co.edu.udistrital.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage MySQL Database connections.
 * Follows the Singleton pattern concept for connection creation.
 */
public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_triage_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 

    /**
     * Establishes and returns a new connection to the MySQL database.
     *
     * @return Connection object to execute SQL queries.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}