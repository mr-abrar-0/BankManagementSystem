package Project_Classes;

import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Bank_DB";
    private static final String USER = "root";
    private static final String PASSWORD = "78900";
    private DatabaseConnection() { }
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "CRITICAL ERROR: Database Connection Failed!\n\n" +
                            "Please verify the following:\n" +
                            "1. MySQL server is running.\n" +
                            "2. The database 'Bank_DB' exists.\n" +
                            "3. Credentials (USER: " + USER + ") are correct.\n\n" +
                            "Details: " + e.getMessage(),
                    "Database Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return connection;
    }
}