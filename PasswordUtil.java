package util;

/**
 * Utility class for password handling
 * Note: This implementation uses plain text passwords for simplicity.
 * In a production environment, passwords should always be hashed.
 */
public class PasswordUtil {

    /**
     * Store password as plain text
     * @param password Plain text password
     * @return The same password (no hashing)
     */
    public static String hashPassword(String password) {
        // Return the password as is (no hashing)
        return password;
    }

    /**
     * Verify a password against a stored password
     * @param password Plain text password to verify
     * @param storedPassword Stored password from the database
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedPassword) {
        // Simple string comparison
        return password != null && password.equals(storedPassword);
    }
}
