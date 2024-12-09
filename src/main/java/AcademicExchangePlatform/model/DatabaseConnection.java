package AcademicExchangePlatform.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class managing database connections.
 * Provides a centralized point for database connection management.
 * Implements thread-safe singleton pattern for database connection handling.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/academicexchangeplatform";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private static DatabaseConnection instance;
    private static Connection connection;
    
    // Static block to load the JDBC driver
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("JDBC Driver loaded successfully!");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
