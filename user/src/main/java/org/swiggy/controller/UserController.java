package org.swiggy.controller;

import org.swiggy.model.User;
import org.swiggy.service.UserService;
import org.swiggy.service.impl2.UserServiceImpl;
import org.swiggy.view.UserDataUpdateType;

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
     * @return The user controller object
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
    public boolean createUser(final User user) {
        return userService.createUser(user);
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
     * Updates the data of the current user.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @param userData Represents the data of the current user to be updated
     * @param type Represents the type of data of the current user to be updated
     */
    public void updateUser(final User user, final String userData, final UserDataUpdateType type) {
        userService.updateUser(user, userData, type);
    }
}
