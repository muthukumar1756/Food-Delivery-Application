package org.swiggy.datahandler.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.exception.UserCreationException;
import org.swiggy.exception.UserNotFoundException;
import org.swiggy.exception.UserUpdateException;
import org.swiggy.datahandler.UserDataHandler;
import org.swiggy.connection.DataBaseConnection;
import org.swiggy.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * Implements the data base service of the user related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class UserDataHandlerImpl implements UserDataHandler {

    private static UserDataHandler userDataHandler;

    private final Logger logger;
    private final Connection connection;

    private UserDataHandlerImpl() {
        logger = LogManager.getLogger(UserDataHandlerImpl.class);
        connection = DataBaseConnection.getConnection();
    }

    /**
     * <p>
     * Gets the object of the user database implementation class.
     * </p>
     *
     * @return The user database service implementation object
     */
    public static UserDataHandler getInstance() {
        if (null == userDataHandler) {
            return userDataHandler = new UserDataHandlerImpl();
        }

        return userDataHandler;
    }

    /**
     * {@inheritDoc}
     *
     * @param user Represents the current {@link User}
     * @return True if user is created, false otherwise
     */
    public boolean createUser(final User user) {
        final String query = """
                insert into users (name, phone_number, email_id, password) values (?, ?, ?, ?) returning id""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPhoneNumber());
            preparedStatement.setString(3, user.getEmailId());
            preparedStatement.setString(4, user.getPassword());
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            final int user_id = resultSet.getInt(1);

            user.setId(user_id);

            return true;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new UserCreationException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param phoneNumber Represents the phone_number of the current user
     * @param password Represents the password of the current user
     * @return The current user
     */
    public User getUser(final String phoneNumber, final String password) {
        final String query = """
                select id, name, phone_number, email_id, password from users where phone_Number = ? and password = ?""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, password);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                final int userid = resultSet.getInt(1);
                final User user = new User(resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5));

                user.setId(userid);

                return user;
            } else {
                return null;
            }
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new UserNotFoundException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param user Represents the current {@link User}
     * @param type Represents the type of data to be updated
     * @param userData Represents the value of data to be updated
     */
    @Override
    public void updateUser(final User user, final String type, final String userData) {
        final String query = String.join("", "update users set ", type, " = ? where id = ?");

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            final int userId = user.getId();

            preparedStatement.setString(1, userData);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new UserUpdateException(message.getMessage());
        }
    }
}
