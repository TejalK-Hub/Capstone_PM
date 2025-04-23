package util;

import model.User;
import service.AuthService;

/**
 * Test class to verify the authentication process
 */
public class AuthenticationTest {
    
    public static void main(String[] args) {
        testAuthentication("admin", "admin123");
        testAuthentication("admin", "wrongpassword");
    }
    
    /**
     * Test authentication with the given username and password
     * @param username Username to test
     * @param password Password to test
     */
    public static void testAuthentication(String username, String password) {
        System.out.println("\nTesting authentication with username: " + username + ", password: " + password);
        
        AuthService authService = new AuthService();
        User user = authService.authenticate(username, password);
        
        if (user != null) {
            System.out.println("Authentication successful!");
            System.out.println("User details:");
            System.out.println("ID: " + user.getUserId());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Role: " + user.getRole());
        } else {
            System.out.println("Authentication failed!");
        }
    }
}
