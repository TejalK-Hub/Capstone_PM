package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Database utility class for managing connections
 */
public class DBUtil {
    private static final String PROPERTIES_FILE = "database.properties";
    private static final String PROPERTIES_PATH = "/WEB-INF/classes/database.properties";
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private static String dbDriver;

    static {
        try {
            // Load database properties
            Properties props = new Properties();
            boolean loaded = false;

            // Try loading from classpath
            InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            if (inputStream != null) {
                props.load(inputStream);
                loaded = true;
                System.out.println("Loaded database properties from classpath");
            } else {
                System.out.println("Could not load database properties from classpath");
            }

            // If not found in classpath, try loading from WEB-INF/classes
            if (!loaded) {
                try {
                    String catalina = System.getProperty("catalina.base");
                    if (catalina != null) {
                        String path = catalina + "/webapps/Project" + PROPERTIES_PATH;
                        File file = new File(path);
                        if (file.exists()) {
                            inputStream = new FileInputStream(file);
                            props.load(inputStream);
                            loaded = true;
                            System.out.println("Loaded database properties from: " + path);
                        } else {
                            System.out.println("Properties file not found at: " + path);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error loading from WEB-INF/classes: " + e.getMessage());
                }
            }

            // If still not loaded, use hardcoded values
            if (!loaded) {
                System.out.println("Using hardcoded database properties");
                dbUrl = "jdbc:mysql://localhost:3306/capstone_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                dbUser = "root";
                dbPassword = "0000";
                dbDriver = "com.mysql.cj.jdbc.Driver";
            } else {
                dbUrl = props.getProperty("db.url");
                dbUser = props.getProperty("db.user");
                dbPassword = props.getProperty("db.password");
                dbDriver = props.getProperty("db.driver");
            }

            // Load JDBC driver
            Class.forName(dbDriver);
            System.out.println("Database driver loaded: " + dbDriver);
            System.out.println("Database URL: " + dbUrl);
            System.out.println("Database User: " + dbUser);

        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Error initializing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    /**
     * Close a database connection
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
