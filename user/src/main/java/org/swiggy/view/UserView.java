package org.swiggy.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.controller.UserController;
import org.swiggy.model.User;
import org.swiggy.model.PasswordGenerator;
import org.swiggy.UserDataValidator;

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
    private final UserDataValidator userDataValidator;

    private UserView() {
        logger = LogManager.getLogger(UserView.class);
        userController = UserController.getInstance();
        userDataValidator = UserDataValidator.getInstance();
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
        logger.info("\n1.Signup\n2.Login\n3.Exit");
        final int userChoice = getChoice();

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

        if (userController.createUser(user)) {
            displayHomePageMenu(user);
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

        if (!userDataValidator.validateUserName(name)) {
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

        if (!userDataValidator.validatePhoneNumber(phoneNumber)) {
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

        if (!userDataValidator.validateEmailId(emailId)) {
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

        if (!userDataValidator.validatePassword(password)) {
            logger.warn("Enter A Valid Password");
            getPassword();
        }

        return PasswordGenerator.getInstance().hashPassword(password);
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
            logger.warn("User Not Registered or Incorrect Password");
            login();
        }
        displayHomePageMenu(currentUser);
    }

    /**
     * <p>
     * Displays restaurants or allows the user to update their profile based on the users input.
     * </p>
     *
     * @param user Represents the current {@link User}
     */
    public void displayHomePageMenu(final User user) {
        logger.info("To Go Back Enter *\n1.Display Restaurants\n2.Edit User Profile\n3.Logout");
        final int userChoice = getChoice();

        if (-1 == userChoice) {
            displayMainMenu();
        }

        switch (userChoice) {
            case 1:
                RestaurantDisplayView.getInstance().displayRestaurants(user);
                break;
            case 2:
                updateUser(user);
                break;
            case 3:
                displayMainMenu();
                break;
            default:
                logger.warn("Enter A Valid Option");
                displayHomePageMenu(user);
        }
    }

    /**
     * <p>
     * Displays the data of current user.
     * </p>
     *
     * @param user Represents the current {@link User}
     */
    private void displayUserData(final User user) {
        final User currentUser = userController.getUser(user.getPhoneNumber(), user.getPassword());

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
     * @param user Represents the current {@link User}
     */
    private void updateUser(final User user) {
        displayUserData(user);
        logger.info("\n1.Update Name\n2.Update Phone Number\n3.Update EmailId\n4.Update Password");
        final int choice = getChoice();

        if (-1 == choice) {
            displayHomePageMenu(user);
        }

        switch (choice) {
            case 1:
                userController.updateUser(user, getName(), UserDataUpdateType.NAME);
                break;
            case 2:
                userController.updateUser(user, getPhoneNumber(), UserDataUpdateType.PHONENUMBER);
                break;
            case 3:
                userController.updateUser(user, getEmailId(), UserDataUpdateType.EMAILID);
                break;
            case 4:
                userController.updateUser(user, getPassword(), UserDataUpdateType.PASSWORD );
                break;
            default:
                logger.warn("Enter A Valid Option");
                updateUser(user);
        }
        updateUser(user);
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
