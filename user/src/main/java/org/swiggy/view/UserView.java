package org.swiggy.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.PasswordHashGenerator;
import org.swiggy.controller.UserController;
import org.swiggy.model.User;
import org.swiggy.DataValidator;

/**
 * <p>
 *  Handles user creation, authentication and updates.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class UserView extends CommonView {

    private static UserView userView;
    private final Logger logger;
    private final UserController userController;
    private final DataValidator dataValidator;

    private UserView() {
        logger = LogManager.getLogger(UserView.class);
        userController = UserController.getInstance();
        dataValidator = DataValidator.getInstance();
    }

    /**
     * <p>
     * Gets the object of the user view class.
     * </p>
     *
     * @return The user view object
     */
    public static UserView getInstance() {
        if (null == userView) {
            userView = new UserView();
        }

        return userView;
    }

    /**
     * <p>
     * Displays the main menu and gets the user choice for signup or login.
     * </p>
     */
    public void displayMainMenu() {
        logger.info("""
                    1.Signup
                    2.Login
                    3.Exit""");
        final int userChoice = getValue();

        switch (userChoice) {
            case 1:
                signUp();
                break;
            case 2:
                login();
                break;
            case 3:
                exit();
                break;
            default:
                logger.warn("Invalid UserChoice");
                displayMainMenu();
        }
    }

    /**
     * <p>
     * Handles the user signup process.
     * </p>
     */
    private void signUp() {
        logger.info("User Signup Or Enter * To Go Back");
        final User user = new User(getName(), getPhoneNumber(), getEmailId(), getPassword());

        if (userController.createUserProfile(user)) {
            displayHomePageMenu(user.getId());
        } else {
            logger.warn("User Already Exists");
            displayMainMenu();
        }
    }

    /**
     * <p>
     * Gets the username from the user after validating the name.
     * </p>
     *
     * @return The valid username of the user
     */
    private String getName() {
        logger.info("Enter Your Name");
        final String name = getInfo();

        backOptionCheck(name);

        if (!dataValidator.validateUserName(name)) {
            logger.warn("Enter A Valid User Name");
            getName();
        }

        return name;
    }

    /**
     * <p>
     * Gets the valid mobile number from the user after validation.
     * </p>
     *
     * @return The mobile number of the user
     */
    private String getPhoneNumber() {
        logger.info("Enter Your Phone Number");
        final String phoneNumber = getInfo();

        backOptionCheck(phoneNumber);

        if (!dataValidator.validatePhoneNumber(phoneNumber)) {
            logger.warn("Enter A Valid Phone Number");
            getPhoneNumber();
        }

        return phoneNumber;
    }

    /**
     * <p>
     * Gets the valid email from the user after validation.
     * </p>
     *
     * @return The valid email of the user
     */
    private String getEmailId() {
        logger.info("Enter Your EmailId");
        final String emailId = getInfo();

        backOptionCheck(emailId);

        if (!dataValidator.validateEmailId(emailId)) {
            logger.warn("Enter A Valid EmailId");
            getEmailId();
        }

        return emailId;
    }

    /**
     * <p>
     * Gets the password from the user after validating the password.
     * </p>
     *
     * @return The validated password of the user
     */
    private String getPassword() {
        logger.info("Enter Your Password");
        final String password = getInfo();

        backOptionCheck(password);

        if (!dataValidator.validatePassword(password)) {
            logger.warn("Enter A Valid Password");
            getPassword();
        }

        return PasswordHashGenerator.getInstance().hashPassword(password);
    }

    /**
     * <p>
     * Checks for the back option
     * </p>
     *
     * @param back Represents the input to checked for the back option
     */
    private void backOptionCheck(final String back) {
        if ("back".equals(back)) {
            displayMainMenu();
        }
    }

    /**
     * <p>
     * Gets the user details for login process.
     * </p>
     */
     private void login() {
        final String phoneNumber = getPhoneNumber();
        final User currentUser = userController.getUser(phoneNumber, getPassword());

        if (null == currentUser) {
            logger.warn("User Not Registered Or Incorrect Password");
            login();
        }
        displayHomePageMenu(currentUser.getId());
    }

    /**
     * <p>
     * Displays restaurants or allows the user to update their profile based on the users input.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    public void displayHomePageMenu(final long userId) {
        logger.info("""
                To Go Back Enter *
                1.Display Restaurants
                2.Edit User Profile
                3.View Orders
                4.Logout""");
        final int userChoice = getValue();

        if (-1 == userChoice) {
            displayMainMenu();
        }

        switch (userChoice) {
            case 1:
                RestaurantDisplayView.getInstance().displayRestaurants(userId);
                break;
            case 2:
                updateUserData(userId);
                break;
            case 3:
                OrderView.getInstance().displayOrders(userId);
                displayHomePageMenu(userId);
                break;
            case 4:
                displayMainMenu();
                break;
            default:
                logger.warn("Enter A Valid Option");
                displayHomePageMenu(userId);
        }
    }

    /**
     * <p>
     * Displays the data of current user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    private void displayUserData(final long userId) {
        final User currentUser = userController.getUserById(userId);

        logger.info("\nYour Current Data\n");
        logger.info(String.format("User Name : %s", currentUser.getName()));
        logger.info(String.format("Phone Number : %s", currentUser.getPhoneNumber()));
        logger.info(String.format("Email Id : %s", currentUser.getEmailId()));
    }

    /**
     * <p>
     * Updates the users information based on the chosen option.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    private void updateUserData(final long userId) {
        displayUserData(userId);
        logger.info("""
                1.Update Name
                2.Update Phone Number
                3.Update EmailId
                4.Update Password""");
        final int choice = getValue();

        if (-1 == choice) {
            displayHomePageMenu(userId);
        }

        switch (choice) {
            case 1:
                userController.updateUserData(userId, getName(), UserDataUpdateType.NAME);
                break;
            case 2:
                userController.updateUserData(userId, getPhoneNumber(), UserDataUpdateType.PHONENUMBER);
                break;
            case 3:
                userController.updateUserData(userId, getEmailId(), UserDataUpdateType.EMAILID);
                break;
            case 4:
                userController.updateUserData(userId, getPassword(), UserDataUpdateType.PASSWORD );
                break;
            default:
                logger.warn("Enter A Valid Option");
                updateUserData(userId);
        }
        updateUserData(userId);
    }

    /**
     * <p>
     * Exits from the application.
     * </p>
     */
    private void exit() {
        System.exit(0);
    }
}
