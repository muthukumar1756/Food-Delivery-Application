package org.swiggy.service;

import org.swiggy.model.User;
import org.swiggy.view.UserDataUpdateType;

/**
 * <p>
 * Provides the services for the user.
 * </p>
 *
 * @author Muthu kumar v
 * @version 1.0
 */
public interface UserService {

    /**
     * <p>
     * Creates the new user.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @return True if user is created, false otherwise
     */
    boolean createUserProfile(final User user);

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
     * Gets the user if the id matches.
     * </p>
     *
     * @param userId Represents the password of the current user
     * @return The current user
     */
    User getUserById(final long userId);

    /**
     * <p>
     * Updates the data of the current user.
     * </p>
     *
     * @param userId Represents the password of the current user
     * @param userData Represents the data of the current user to be updated
     * @param type Represents the type of data of the current user to be updated
     */
    void updateUserData(final long userId, final String userData, final UserDataUpdateType type);
}
