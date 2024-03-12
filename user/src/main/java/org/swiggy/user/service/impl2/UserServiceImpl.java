package org.swiggy.user.service.impl2;

import org.swiggy.user.datahandler.UserDataHandler;
import org.swiggy.user.datahandler.impl.UserDataHandlerImpl;
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
    private final UserDataHandler userDataHandler;

    private UserServiceImpl() {
        userDataHandler = UserDataHandlerImpl.getInstance();
    }

    /**
     * <p>
     * Gets the object of the user service implementation class.
     * </p>
     *
     * @return The user service implementation class object
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
     * @param type Represents the type of user data to be updated
     * @param userData Represents the data of the user to be updated
     * @return True if user data is updated, false otherwise
     */
    @Override
    public boolean updateUserData(final long userId, final UserDataUpdateType type, final String userData){
        switch (type) {
            case NAME:
                return userDataHandler.updateUserProfile(userId, "name", userData);
            case PHONENUMBER:
                return userDataHandler.updateUserProfile(userId, "phone_number", userData);
            case EMAILID :
                return userDataHandler.updateUserProfile(userId, "email_id", userData);
            case PASSWORD:
                return userDataHandler.updateUserProfile(userId, "password", userData);
            default:
                throw new IllegalArgumentException("Invalid UserDataUpdateType: " + type);
        }
    }
}
