package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Update database.properties file with the correct password
 */
public class UpdateDatabaseProperties {
    
    private static final String PROPERTIES_FILE = "src/main/resources/database.properties";
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java UpdateDatabaseProperties <password>");
            System.out.println("Example: java UpdateDatabaseProperties root");
            return;
        }
        
        String newPassword = args[0];
        
        try {
            // Load properties file
            Properties props = new Properties();
            InputStream input = new FileInputStream(PROPERTIES_FILE);
            props.load(input);
            input.close();
            
            // Get current values
            String currentUrl = props.getProperty("db.url");
            String currentUser = props.getProperty("db.user");
            String currentPassword = props.getProperty("db.password");
            String currentDriver = props.getProperty("db.driver");
            
            System.out.println("Current database properties:");
            System.out.println("URL: " + currentUrl);
            System.out.println("User: " + currentUser);
            System.out.println("Password: " + currentPassword);
            System.out.println("Driver: " + currentDriver);
            
            // Update password
            props.setProperty("db.password", newPassword);
            
            // Save properties file
            OutputStream output = new FileOutputStream(PROPERTIES_FILE);
            props.store(output, "Database Connection Properties");
            output.close();
            
            System.out.println("\nDatabase properties updated successfully!");
            System.out.println("New password: " + newPassword);
            
        } catch (IOException e) {
            System.err.println("Error updating database properties!");
            e.printStackTrace();
        }
    }
}
