package org.swiggy.datahandler;

import org.swiggy.model.User;

/**
 * <p>
 * Provides data base service for the user
 * </p>
 *
 * @author Muthu kumar v
 * @version 1.1
 */
public interface UserDataHandler {

    /**
     * <p>
     * Creates the new user.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @return True if user is created, false otherwise
     */
    boolean createUser(final User user);

    /**
     * <p>
     * Gets the user if the phone_number and password matches.
     * </p>
     *
     * @param phoneNumber Represents the phone_number of the current user
     * @param password Represents the password of the current user
     * @return The current user
     */
    User getUser(final String phoneNumber, final String password);

    /**
     * <p>
     * Updates the data of the current user.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @param type Represents the type of data to be updated
     * @param userData Represents the data to be updated
     */
    void updateUser(final User user, final String type, final String userData);
}
