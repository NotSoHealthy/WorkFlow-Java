package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String url = "jdbc:mysql://localhost:3306/workflow";
    private final String username = "root";
    private final String password = "admin";

    private Connection cnx;
    private static DBConnection instance;

    private DBConnection() {
        try {
            cnx = DriverManager.getConnection(url, username, password);
            System.out.println("Connection established !");
        } catch (SQLException e) {
            System.out.println("Error while trying to establish connection : " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = DriverManager.getConnection(url, username, password);
                System.out.println("Reconnecting: Connection established !");
            }
        } catch (SQLException e) {
            System.out.println("Failed to re-establish connection: " + e.getMessage());
        }
        return cnx;
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
}