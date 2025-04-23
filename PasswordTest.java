package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Test database connection with different passwords
 */
public class PasswordTest {
    
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    
    // Passwords to try
    private static final String[] PASSWORDS = {
        "0000",
        "",
        "root",
        "password",
        "admin",
        "mysql"
    };
    
    public static void main(String[] args) {
        try {
            System.out.println("Loading MySQL JDBC driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            System.out.println("Testing database connection with different passwords...");
            System.out.println("URL: " + DB_URL);
            System.out.println("User: " + DB_USER);
            
            boolean connected = false;
            String workingPassword = null;
            
            for (String password : PASSWORDS) {
                try {
                    System.out.println("\nTrying password: \"" + password + "\"");
                    Connection conn = DriverManager.getConnection(DB_URL, DB_USER, password);
                    System.out.println("Connection successful with password: \"" + password + "\"");
                    
                    // Print connection details
                    System.out.println("Connection URL: " + conn.getMetaData().getURL());
                    System.out.println("Database product: " + conn.getMetaData().getDatabaseProductName());
                    System.out.println("Database version: " + conn.getMetaData().getDatabaseProductVersion());
                    
                    conn.close();
                    connected = true;
                    workingPassword = password;
                    break;
                } catch (Exception e) {
                    System.out.println("Connection failed with password: \"" + password + "\"");
                    System.out.println("Error: " + e.getMessage());
                }
            }
            
            if (connected) {
                System.out.println("\n==============================================");
                System.out.println("SUCCESS! Connected with password: \"" + workingPassword + "\"");
                System.out.println("Update your database.properties file with this password.");
                System.out.println("==============================================");
            } else {
                System.out.println("\n==============================================");
                System.out.println("FAILED! Could not connect with any of the tried passwords.");
                System.out.println("Please try with your actual MySQL root password.");
                System.out.println("==============================================");
            }
            
        } catch (Exception e) {
            System.err.println("Error loading JDBC driver!");
            e.printStackTrace();
        }
    }
}
