package org.swiggy.datahandler.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.swiggy.exception.UserDataLoadFailureException;
import org.swiggy.exception.UserDataNotFoundException;
import org.swiggy.exception.UserDataUpdateFailureException;
import org.swiggy.datahandler.UserDataHandler;
import org.swiggy.connection.DataBaseConnection;
import org.swiggy.model.User;

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
    public boolean createUserProfile(final User user) {
        final String query = """
                insert into users (name, phone_number, email_id, password) values (?, ?, ?, ?) returning id""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPhoneNumber());
            preparedStatement.setString(3, user.getEmailId());
            preparedStatement.setString(4, user.getPassword());
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            user.setId(resultSet.getInt(1));

            return true;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new UserDataLoadFailureException(message.getMessage());
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
                final User user = new User();

                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setPhoneNumber(resultSet.getString(3));
                user.setEmailId(resultSet.getString(4));
                user.setPassword(resultSet.getString(5));

                return user;
            } else {
                return null;
            }
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new UserDataNotFoundException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the current user
     * @return The current user
     */
    @Override
    public User getUserById(final long userId) {
        final String query = """
                select id, name, phone_number, email_id, password from users where id = ?""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                final User user = new User();

                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setPhoneNumber(resultSet.getString(3));
                user.setEmailId(resultSet.getString(4));
                user.setPassword(resultSet.getString(5));

                return user;
            } else {
                return null;
            }
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new UserDataNotFoundException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of current {@link User}
     * @param type Represents the type of data to be updated
     * @param userData Represents the value of data to be updated
     */
    @Override
    public void updateUserProfile(final long userId, final String type, final String userData) {
        final String query = String.join("", "update users set ", type, " = ? where id = ?");

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userData);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new UserDataUpdateFailureException(message.getMessage());
        }
    }
}