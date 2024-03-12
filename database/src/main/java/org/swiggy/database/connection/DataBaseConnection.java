package org.swiggy.database.connection;

import org.swiggy.database.exception.DatabaseConnectionFailureException;

import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <p>
 * Connects with database to store information.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class DataBaseConnection {

    private static Connection connection;

    private DataBaseConnection() {
    }

    /**
     * <p>
     * Gets the database connection.
     * </p>
     *
     * @return The database connection
     */
    public static Connection getConnection() {

        if (null == connection) {

            try (final InputStream inputStream = Class.forName(DataBaseConnection.class.getName())
                    .getClassLoader().getResourceAsStream("Database.properties")) {
                final Properties properties = new Properties();

                properties.load(inputStream);
                final String url = properties.get("url").toString();
                final String user = properties.get("user").toString();
                final String password = properties.get("password").toString();

                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException | IOException | ClassNotFoundException message) {
                LogManager.getLogger(DataBaseConnection.class).warn(message.getMessage());
                throw new DatabaseConnectionFailureException(message.getMessage());
            }
        }

        return connection;
    }
}