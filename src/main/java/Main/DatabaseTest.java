package Main;

import database.DataAccessException;
import database.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        // Attempt to establish a database connection
        try (Connection connection = DatabaseManager.INSTANCE.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Connection to the database successfully established!");
            } else {
                System.out.println("A connection could not be established.");
            }
        } catch (SQLException e) {
            System.out.println("SQL-Error: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("DataAccessException: " + e.getMessage());
            e.printStackTrace();
        }
    }
}