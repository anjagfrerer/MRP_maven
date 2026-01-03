package Main;

import database.DataAccessException;
import database.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        // Versuch, eine Datenbankverbindung herzustellen
        try (Connection connection = DatabaseManager.INSTANCE.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Verbindung zur Datenbank erfolgreich hergestellt!");
            } else {
                System.out.println("Verbindung konnte nicht hergestellt werden.");
            }
        } catch (SQLException e) {
            System.out.println("SQL-Fehler: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("DataAccessException: " + e.getMessage());
            e.printStackTrace();
        }
    }
}