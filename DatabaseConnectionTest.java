package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Test class to verify database connection and query the users table
 */
public class DatabaseConnectionTest {
    
    public static void main(String[] args) {
        testConnection();
        testQueryUsers();
    }
    
    /**
     * Test database connection
     */
    public static void testConnection() {
        Connection conn = null;
        try {
            System.out.println("Testing database connection...");
            conn = DBUtil.getConnection();
            System.out.println("Connection successful!");
            
            // Print connection details
            System.out.println("Connection URL: " + conn.getMetaData().getURL());
            System.out.println("Database product: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("Database version: " + conn.getMetaData().getDatabaseProductVersion());
            
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
    }
    
    /**
     * Test querying the users table
     */
    public static void testQueryUsers() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            System.out.println("\nTesting query on users table...");
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            
            // First check if the database exists
            rs = stmt.executeQuery("SHOW DATABASES LIKE 'capstone_db'");
            if (!rs.next()) {
                System.err.println("Database 'capstone_db' does not exist!");
                return;
            }
            
            // Check if the users table exists
            rs = stmt.executeQuery("SHOW TABLES FROM capstone_db LIKE 'users'");
            if (!rs.next()) {
                System.err.println("Table 'users' does not exist in database 'capstone_db'!");
                return;
            }
            
            // Query the users table
            rs = stmt.executeQuery("SELECT user_id, username, password, role FROM users");
            System.out.println("Users in the database:");
            System.out.println("ID | Username | Password | Role");
            System.out.println("--------------------------------");
            
            boolean hasUsers = false;
            while (rs.next()) {
                hasUsers = true;
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                
                System.out.println(userId + " | " + username + " | " + password + " | " + role);
            }
            
            if (!hasUsers) {
                System.out.println("No users found in the database!");
            }
            
        } catch (SQLException e) {
            System.err.println("Query failed!");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                DBUtil.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
