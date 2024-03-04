package org.swiggy.service.impl2;

import org.swiggy.datahandler.UserDataHandler;
import org.swiggy.datahandler.impl.UserDataHandlerImpl;
import org.swiggy.model.User;
import org.swiggy.service.UserService;
import org.swiggy.view.UserDataUpdateType;

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
    private final UserDataHandler userDataHandler;

    private UserServiceImpl() {
        userDataHandler = UserDataHandlerImpl.getInstance();
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
       return userDataHandler.createUserProfile(user);
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
        return userDataHandler.getUser(phoneNumber, password);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the password of the current user
     * @return The current user
     */
    @Override
    public User getUserById(final long userId) {
        return userDataHandler.getUserById(userId);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of current {@link User}
     * @param type Represents the user data to be updated
     */
    @Override
    public void updateUserData(final long userId, final String userData, final UserDataUpdateType type){
        switch (type) {
            case NAME:
                userDataHandler.updateUserProfile(userId, "name", userData);
                break;
            case PHONENUMBER:
                userDataHandler.updateUserProfile(userId, "phone_number", userData);
                break;
            case EMAILID :
                userDataHandler.updateUserProfile(userId, "email_id", userData);
                break;
            case PASSWORD:
                userDataHandler.updateUserProfile(userId, "password", userData);
                break;
        }
    }
}
