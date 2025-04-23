package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Simple database connection test
 */
public class SimpleDBTest {
    
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/capstone_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "0000"; // Change this to your actual MySQL password
    
    public static void main(String[] args) {
        try {
            System.out.println("Loading MySQL JDBC driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            System.out.println("Connecting to database...");
            System.out.println("URL: " + DB_URL);
            System.out.println("User: " + DB_USER);
            System.out.println("Password: " + DB_PASSWORD);
            
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connection successful!");
            
            // Print connection details
            System.out.println("Connection URL: " + conn.getMetaData().getURL());
            System.out.println("Database product: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("Database version: " + conn.getMetaData().getDatabaseProductVersion());
            
            // Test a simple query
            Statement stmt = conn.createStatement();
            
            // Check if the database exists
            ResultSet rs = stmt.executeQuery("SHOW DATABASES");
            System.out.println("\nDatabases:");
            while (rs.next()) {
                System.out.println("- " + rs.getString(1));
            }
            
            // Check if the capstone_db database exists
            rs = stmt.executeQuery("SHOW DATABASES LIKE 'capstone_db'");
            if (rs.next()) {
                System.out.println("\nDatabase 'capstone_db' exists!");
            } else {
                System.out.println("\nDatabase 'capstone_db' does NOT exist!");
                System.out.println("Creating database 'capstone_db'...");
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS capstone_db");
                System.out.println("Database created successfully!");
            }
            
            // Use the capstone_db database
            stmt.executeUpdate("USE capstone_db");
            
            // Check if the users table exists
            rs = stmt.executeQuery("SHOW TABLES LIKE 'users'");
            if (rs.next()) {
                System.out.println("Table 'users' exists!");
                
                // Check if the admin user exists
                rs = stmt.executeQuery("SELECT * FROM users WHERE username = 'admin'");
                if (rs.next()) {
                    System.out.println("Admin user exists!");
                    System.out.println("Username: " + rs.getString("username"));
                    System.out.println("Password: " + rs.getString("password"));
                    System.out.println("Role: " + rs.getString("role"));
                } else {
                    System.out.println("Admin user does NOT exist!");
                    System.out.println("Creating admin user...");
                    stmt.executeUpdate("INSERT INTO users (username, password, email, first_name, last_name, role) " +
                                      "VALUES ('admin', 'admin123', 'admin@example.com', 'System', 'Admin', 'admin')");
                    System.out.println("Admin user created successfully!");
                }
            } else {
                System.out.println("Table 'users' does NOT exist!");
                System.out.println("You need to run the database setup script first.");
            }
            
            // Close resources
            rs.close();
            stmt.close();
            conn.close();
            System.out.println("\nConnection closed.");
            
        } catch (Exception e) {
            System.err.println("Database connection error!");
            e.printStackTrace();
        }
    }
}
