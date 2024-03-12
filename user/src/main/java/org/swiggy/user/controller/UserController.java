package org.swiggy.user.controller;

import org.swiggy.user.model.User;
import org.swiggy.user.service.UserService;
import org.swiggy.user.service.impl2.UserServiceImpl;
import org.swiggy.user.view.UserDataUpdateType;

/**
 * <p>
 * Handles the user related operation and responsible for processing user input.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class UserController {

    private static UserController userController;
    private final UserService userService;

    private UserController() {
        userService = UserServiceImpl.getInstance();
    }

    /**
     * <p>
     * Gets the object of the user controller class.
     * </p>
     *
     * @return The user controller class object
     */
    public static UserController getInstance() {
        if (null == userController) {
            userController = new UserController();
        }

        return userController;
    }

    /**
     * <p>
     * Creates the new user.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @return True if user is created, false otherwise
     */
    public boolean createUserProfile(final User user) {
        return userService.createUserProfile(user);
    }

    /**
     * <p>
     * Gets the user if the phone_number and password matches.
     * </p>
     *
     * @param phoneNumber Represents the phone_number of the current user
     * @param password Represents the password of the current user
     * @return The current user
     */
    public User getUser(final String phoneNumber, final String password) {
        return userService.getUser(phoneNumber, password);
    }

    /**
     * <p>
     * Gets the user if the id matches.
     * </p>
     *
     * @param userId Represents the password of the current user
     * @return The current user
     */
    public User getUserById(final long userId) {
        return userService.getUserById(userId);
    }

    /**
     * <p>
     * Updates the data of the current user.
     * </p>
     *
     * @param userId Represents the id 0f the current {@link User}
     * @param type Represents the type of data of the current user to be updated
     * @param userData Represents the data of the current user to be updated
     */
    public void updateUserData(final long userId, final UserDataUpdateType type, final String userData) {
        userService.updateUserData(userId, type, userData);
    }
}
