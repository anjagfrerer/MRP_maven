package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This enum manages the database connection.
 * It uses a single instance (INSTANCE) for the whole application.
 */
public enum DatabaseManager {
    INSTANCE;

    /**
     * Creates and returns a connection to the database.
     *
     * @return a Connection to the PostgreSQL database
     */
    public Connection getConnection()
    {
        try {
            // Uses JDBC to connect to the PostgreSQL database
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/swen1",
                    "postgres",
                    "mysecretpassword");
        } catch (SQLException e) {
            throw new DataAccessException("Database connection failed", e);
        }
    }
}