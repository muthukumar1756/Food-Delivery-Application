package org.swiggy.user.service.impl1;

import java.util.HashMap;
import java.util.Map;

import org.swiggy.user.model.User;
import org.swiggy.user.service.UserService;
import org.swiggy.user.view.UserDataUpdateType;

/**
 * <p>
 * Implements the service of the user related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class UserServiceImpl implements UserService {

    private static UserService userService;

    private final Map<String, User> users = new HashMap<>();

    private UserServiceImpl() {
    }

    /**
     * <p>
     * Gets the object of the user service implementation class.
     * </p>
     *
     * @return The user service implementation object
     */
    public static UserService getInstance() {
        if (null == userService) {
            userService = new UserServiceImpl();
        }

        return userService;
    }

    /**
     * {@inheritDoc}
     *
     * @param user Represents the current {@link User}
     * @return True if user is created, false otherwise
     */
    @Override
    public boolean createUserProfile(final User user) {
        if (!users.containsKey(user.getPhoneNumber())) {
            if(!users.containsKey((user.getEmailId()))) {
                users.put(user.getPhoneNumber(), user);

                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param phoneNumber Represents the phone_number of the current user
     * @param password Represents the password of the current user
     * @return The current user
     */
    @Override
    public User getUser(final String phoneNumber, final String password) {
        final User currentUser = users.get(phoneNumber);
        final String currentUserPassword = currentUser.getPassword();

        if (password.equals(currentUserPassword)) {
            return currentUser;
        } else {
            return null;
        }
    }

    @Override
    public User getUserById(final long userId) {
        for (final User user : users.values()) {

            if (user.getId() == userId) {
                return user;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of current {@link User}
     * @param type Represents the user data to be updated
     */
    public boolean updateUserData(final long userId, final UserDataUpdateType type, final String userData) {
        User currentUser = null;

        for (final User user : users.values()) {

            if (user.getId() == userId) {
                currentUser = user;
            }
        }

        switch (type) {
            case NAME:
                currentUser.setName(userData);
                break;
            case PHONENUMBER:
                currentUser.setPhoneNumber(userData);
                break;
            case EMAILID:
                currentUser.setEmailId(userData);
                break;
            case PASSWORD:
                currentUser.setPassword(userData);
                break;
        }

        return false;
    }
}
