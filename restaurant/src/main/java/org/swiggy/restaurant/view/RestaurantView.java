package org.swiggy.restaurant.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import org.swiggy.common.hashgenerator.PasswordHashGenerator;
import org.swiggy.validator.DataValidator;
import org.swiggy.restaurant.controller.RestaurantController;
import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.FoodType;
import org.swiggy.restaurant.model.Restaurant;
import org.swiggy.restaurant.model.RestaurantDataUpdateType;
import org.swiggy.common.view.CommonView;
import org.swiggy.common.view.CommonViewImpl;

/**
 * <p>
 * Displays restaurants details and menucard of the selected restaurant.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class RestaurantView {

    private static RestaurantView restaurantView;
    private final CommonView commonView;
    private final Logger logger;
    private final RestaurantController restaurantController;
    private final DataValidator dataValidator;

    private RestaurantView() {
        logger = LogManager.getLogger(RestaurantView.class);
        commonView = CommonViewImpl.getInstance();
        restaurantController = RestaurantController.getInstance();
        dataValidator = DataValidator.getInstance();
    }

    /**
     * <p>
     * Gets the object of the restaurant view class.
     * </p>
     *
     * @return The restaurant view object
     */
    public static RestaurantView getInstance() {
        if (null == restaurantView) {
            restaurantView = new RestaurantView();
        }

        return restaurantView;
    }

    /**
     * <p>
     * Displays the main menu and gets the restaurant user choice for signup or login.
     * </p>
     */
    public void displayMainMenu() {
        logger.info("""
                1.Signup
                2.Login
                3.Exit""");
        final int userChoice = commonView.getValue();

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
                logger.warn("Invalid Choice");
                displayMainMenu();
        }
    }

    /**
     * <p>
     * Handles the restaurant user signup process.
     * </p>
     */
    private void signUp() {
        logger.info("Restaurant Signup Or Enter * To Go Back");
        final Restaurant restaurant = new Restaurant(getName(), getPhoneNumber(), getEmailId(), getPassword());

        if (restaurantController.createRestaurantProfile(restaurant)) {
            logger.info("You Have To Add Atleast One Food From Your Restaurant");
            addFood(restaurant.getRestaurantId());
            displayHomePageMenu(restaurant.getRestaurantId());
        } else {
            logger.warn("Restaurant Already Exists");
            displayMainMenu();
        }
    }

    /**
     * <p>
     * Gets the valid username from the restaurant user.
     * </p>
     *
     * @return The valid username of the user
     */
    private String getName() {
        logger.info("Enter Your Name");
        final String name = commonView.getInfo();

        backOptionCheck(name);

        if (!dataValidator.validateUserName(name)) {
            logger.warn("Enter A Valid Restaurant Name");
            getName();
        }

        return name;
    }

    /**
     * <p>
     * Gets the valid mobile number from the restaurant user.
     * </p>
     *
     * @return The mobile number of the restaurant user
     */
    private String getPhoneNumber() {
        logger.info("Enter Your Phone Number");
        final String phoneNumber = commonView.getInfo();

        backOptionCheck(phoneNumber);

        if (!dataValidator.validatePhoneNumber(phoneNumber)) {
            logger.warn("Enter A Valid Phone Number");
            getPhoneNumber();
        }

        return phoneNumber;
    }

    /**
     * <p>
     * Gets the valid email from the restaurant user.
     * </p>
     *
     * @return The valid email of the restaurant user
     */
    private String getEmailId() {
        logger.info("Enter Your EmailId");
        final String emailId = commonView.getInfo();

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
     * @return The validated password of the restaurant
     */
    private String getPassword() {
        logger.info("Enter Your Password");
        final String password = commonView.getInfo();

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
     * Gets the restaurant details for login process.
     * </p>
     */
    private void login() {
        final String phoneNumber = getPhoneNumber();
        final Restaurant restaurant = restaurantController.getRestaurant(phoneNumber, getPassword());

        if (null == restaurant) {
            logger.warn("Restaurant Not Registered or Incorrect Password");
            login();
        }
        displayHomePageMenu(restaurant.getRestaurantId());
    }

    /**
     * <p>
     * Displays the home page for the restaurant user.
     * </p>
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     */
    private void displayHomePageMenu(final long restaurantId) {
        logger.info("""
                To Go Back Enter *
                1.Add Food
                2.Remove Food
                3.Edit Profile
                4.Logout""");
        final int userChoice = commonView.getValue();

        if (-1 == userChoice) {
            displayMainMenu();
        }

        switch (userChoice) {
            case 1:
                addFood(restaurantId);
                break;
            case 2:
                removeFood(restaurantId);
                break;
            case 3:
                updateRestaurantData(restaurantId);
                break;
            case 4:
                displayMainMenu();
                break;
            default:
                logger.info("Enter Valid Input");
                displayHomePageMenu(restaurantId);
        }
    }

    /**
     * <p>
     * Gets the restaurant details for login process.
     * </p>
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     * */
    private void addFood(final long restaurantId) {
        logger.info("Enter The Food Name");
        final String name = commonView.getInfo();

        logger.info("Enter The Food Rate");
        final float rate = commonView.getValue();

        logger.info("Enter The Food Quantity");
        final int quantity = commonView.getValue();

        restaurantController.addFood(new Food(name, rate, getType(), quantity), restaurantId);
        displayHomePageMenu(restaurantId);
    }

    /**
     * <p>
     * Gets the Food type from the restaurant user.
     * </p>
     *
     * @return The Food type id.
     */
    private FoodType getType() {
        logger.info("""
                Enter the Food Type
                1.Veg
                2.NonVeg""");
        final FoodType foodType = FoodType.getTypeById(commonView.getValue());

        if (null == foodType) {
            logger.warn("Enter A Valid Id");
            getType();
        }

        return foodType;
    }

    /**
     * <p>
     * Removes the food from the restaurant menucard.
     * </p>
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     */
    private void removeFood(final long restaurantId) {
        logger.info("""
                Enter Type Of Food To Be Removed
                1.VEG
                2.NONVEG
                3.VEG & NONVEG""");
        final int foodType = commonView.getValue();

        if (-1 == foodType) {
            displayHomePageMenu(restaurantId);
        }

        if (0 < foodType && 4 > foodType) {
            final List<Food> menucard = getMenucard(restaurantId, foodType);

            if (null == menucard) {
                logger.warn("Your Restaurant Currently Doesn't Have Any Available Items");
                displayHomePageMenu(restaurantId);
            }
            displayFoods(menucard);
            logger.info("Enter FoodId To Remove");
            final long foodId = selectFood(restaurantId, menucard);

            restaurantController.removeFood(foodId);
            logger.info("The Food Was Removed Successfully");
            displayHomePageMenu(restaurantId);
        } else {
            logger.warn("Enter A Valid Option");
            removeFood(restaurantId);
        }
    }

    /**
     * <p>
     * Displays the menucard of the restaurant.
     * </p>
     *
     * @param menucard Represents the menucard of the restaurant
     */
    public void displayFoods(final List<Food> menucard) {
        logger.info("""
                Available Items:
                ID | Name | Rate | Category""");

        for (final Food food : menucard) {
            logger.info(String.format("%d %s %.2f %s", menucard.indexOf(food) + 1,
                    food.getFoodName(), food.getRate(), food.getType()));
        }
    }

    /**
     * <p>
     * Gets the selection of food from the user.
     * </p>
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     * @param menucard Represents the menucard of the selected restaurant
     */
    private long selectFood(final long restaurantId, final List<Food> menucard) {
        final int selectedIndex = commonView.getValue();

        if (-1 == selectedIndex) {
            displayHomePageMenu(restaurantId);
        }
        final int foodNumber = selectedIndex - 1;

        if (0 <= foodNumber && menucard.size() >= foodNumber) {
            final Food selectedFood = menucard.get(foodNumber);

            return selectedFood.getFoodId();
        } else {
            logger.warn("Enter A Valid Option From The Menucard");
            return selectFood(restaurantId, menucard);
        }
    }

    /**
     * <p>
     * Gets all the restaurants.
     * </p>
     *
     * @return The map having all the restaurants
     */
    public List<Restaurant> getRestaurants() {
        return restaurantController.getRestaurants();
    }

    /**
     * <p>
     * Gets the selection of a restaurant by the user.
     * </p>
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     */
    public List<Food> getMenucard(final long restaurantId, final int foodType) {
        return restaurantController.getMenuCard(restaurantId, foodType);
    }

    /**
     * <p>
     * Gets the selection of a restaurant by the user.
     * </p>
     *
     * @param foodId Represents the id of the current {@link Food} selected by the user
     * @return Available quantity from the selected restaurant
     */
    public int getQuantity(final long foodId) {
        return restaurantController.getQuantity(foodId);
    }

    /**
     * <p>
     * Displays the data of current restaurant user.
     * </p>
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     */
    private void displayRestaurantData(final long restaurantId) {
        final Restaurant restaurant = restaurantController.getRestaurantById(restaurantId);

        logger.info("\nYour Current Data\n");
        logger.info(String.format("Name : %s", restaurant.getName()));
        logger.info(String.format("Phone Number : %s", restaurant.getPhoneNumber()));
        logger.info(String.format("Email Id : %s", restaurant.getEmailId()));
    }

    /**
     * <p>
     * Updates the restaurant user information based on the chosen option.
     * </p>
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     */
    private void updateRestaurantData(final long restaurantId) {
        displayRestaurantData(restaurantId);
        logger.info("""
                1.Update Name
                2.Update Phone Number
                3.Update EmailId
                4.Update Password""");
        final int choice = commonView.getValue();

        if (-1 == choice) {
            displayHomePageMenu(restaurantId);
        }

        switch (choice) {
            case 1:
                restaurantController.updateRestaurantData(restaurantId, getName(), RestaurantDataUpdateType.NAME);
                break;
            case 2:
                restaurantController.updateRestaurantData(restaurantId, getPhoneNumber(), RestaurantDataUpdateType.PHONENUMBER);
                break;
            case 3:
                restaurantController.updateRestaurantData(restaurantId, getEmailId(), RestaurantDataUpdateType.EMAILID);
                break;
            case 4:
                restaurantController.updateRestaurantData(restaurantId, getPassword(), RestaurantDataUpdateType.PASSWORD );
                break;
            default:
                logger.warn("Enter A Valid Option");
                updateRestaurantData(restaurantId);
        }
        updateRestaurantData(restaurantId);
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