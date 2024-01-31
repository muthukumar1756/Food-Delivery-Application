package org.swiggy.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p>
 * Connects with database to store information.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class DataBaseConnection {

    private static final String url = "jdbc:postgresql://localhost/com.swiggy";
    private static final String user = "postgres";
    private static final String password = "123";

    private static Connection connection;

    private DataBaseConnection(){
    }

    /**
     * <p>
     * Gets the database connection.
     * </p>
     * @return The database connection
     */
    public static Connection getConnection() {
        if (null == connection) {

            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return connection;
    }
}
