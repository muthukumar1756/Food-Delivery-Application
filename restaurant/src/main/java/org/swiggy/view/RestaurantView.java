package org.swiggy.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import org.swiggy.controller.RestaurantController;
import org.swiggy.model.Food;
import org.swiggy.model.Restaurant;

/**
 * <p>
 * Displays restaurants details and menucard of the selected restaurant.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class RestaurantView extends CommonView {

    private static RestaurantView restaurantView;

    private final Logger logger;
    private final RestaurantController restaurantController;

    private RestaurantView() {
        logger = LogManager.getLogger(RestaurantView.class);
        restaurantController = RestaurantController.getInstance();
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
     * Displays the available restaurants.
     * </p>
     */
    public void displayRestaurants() {
        logger.info("To Go Back Enter *\nAvailable Restaurants In Your Area:");

        for (final Map.Entry<Integer, Restaurant> restaurants : restaurantController.getRestaurants().entrySet()) {
            logger.info(String.format("%d %s", restaurants.getKey(), restaurants.getValue().getName()));
        }
    }

    /**
     * <p>
     * Gets the selection of a restaurant by the user.
     * </p>
     */
    public Restaurant getRestaurant(final int restaurantId) {
        return restaurantController.getRestaurants().get(restaurantId);
    }

    /**
     * <p>
     * Gets the selection of a restaurant by the user.
     * </p>
     */
    public List<Food> getMenucard(final Restaurant restaurant) {
        return restaurantController.getMenuCard(restaurant);
    }

    /**
     * <p>
     * Gets the veg menucard of a restaurant.
     * </p>
     */
    public List<Food> getVegMenucard(final Restaurant restaurant) {
        return restaurant.getVegMenucard();
    }

    /**
     * <p>
     * Gets the non veg menucard of a restaurant.
     * </p>
     */
    public List<Food> getNonVegMenucard(final Restaurant restaurant) {
        return restaurant.getNonVegMenucard();
    }

    /**
     * <p>
     * Displays the menucard of the restaurant selected by the user.
     * </p>
     *
     * @param menucard Represents the menucard from the selected restaurant
     */
    public void displayMenucard(final List<Food> menucard) {
        logger.info("\nAvailable Items:\n");
        logger.info("ID | Name | Rate | Category ");

        for (final Food food : menucard) {
            logger.info(String.format("%d %s %.2f %s", menucard.indexOf(food) + 1,
                    food.getFoodName(), food.getRate(), food.getType()));
        }
    }

    /**
     * <p>
     * Gets the selection of a restaurant by the user.
     * </p>
     */
    public int getQuantity(final Food food, final int quantity) {
        return restaurantController.getQuantity(food, quantity);
    }
}