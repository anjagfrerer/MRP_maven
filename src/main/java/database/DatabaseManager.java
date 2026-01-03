package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Im Enum DatabaseManager werden alle Konstanten (hier nur INSTANCE) automatisch als public static final Objekte erzeugt.
 * Das heißt, DatabaseManager.INSTANCE ist eine einzige, globale Instanz des DatabaseManager und man kann keine weiteren erzeugen.
 */
public enum DatabaseManager {
    INSTANCE;

    public Connection getConnection()
    {
        try {
            // JDBC-API (DriverManager), um sich mit der PostgreSQL-Datenbank zu verbinden
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/swen1",
                    "postgres",
                    "mysecretpassword");
        } catch (SQLException e) {
            throw new DataAccessException("Datenbankverbindungsaufbau nicht erfolgreich", e);
        }
    }
}