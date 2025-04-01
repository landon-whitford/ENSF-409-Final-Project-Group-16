package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnector is a singleton class that manages database connections
 * for the Accessible Transportation Scheduler application.
 */
public class DatabaseConnector {
    // Database connection parameters
    private static final String URL = "jdbc:postgresql://localhost/accessibletransportation";
    private static final String USERNAME = "oop";
    private static final String PASSWORD = "ucalgary";

    // Singleton instance
    private static Connection instance = null;

    /**
     * Private constructor to prevent instantiation
     */
    private DatabaseConnector() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Get a connection to the database.
     * If a connection doesn't exist, creates one. Otherwise, returns the existing connection.
     *
     * @return The database connection
     * @throws SQLException If a database error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (instance == null || instance.isClosed()) {
            try {
                // Load the PostgreSQL JDBC driver
                Class.forName("org.postgresql.Driver");

                // Create a new connection
                instance = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                instance.setAutoCommit(true);
            } catch (ClassNotFoundException e) {
                throw new SQLException("PostgreSQL JDBC driver not found", e);
            }
        }
        return instance;
    }

    /**
     * Closes the database connection if it exists and is open.
     *
     * @throws SQLException If a database error occurs
     */
    public static void closeConnection() throws SQLException {
        if (instance != null && !instance.isClosed()) {
            instance.close();
            instance = null;
        }
    }
}