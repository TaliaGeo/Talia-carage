package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/anas";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Private constructor to prevent instantiation
    private DBUtil() { }

    // This method creates a new connection every time it is called
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL driver
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1); // Exit if database connection fails
        }
        return null; // This should never be reached due to System.exit
    }
}
