package org.swiggy.controller;

import org.swiggy.model.Food;
import org.swiggy.model.Restaurant;
import org.swiggy.service.RestaurantService;
import org.swiggy.service.impl2.RestaurantServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Handles the restaurant related operation and responsible for receiving user input and processing it.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class RestaurantController {

    private static RestaurantController restaurantController;

    private final RestaurantService restaurantService;

    private RestaurantController() {
        restaurantService = RestaurantServiceImpl.getInstance();
    }

    /**
     * <p>
     * Gets the restaurant controller object.
     * </p>
     *
     * @return The restaurant controller object
     */
    public static RestaurantController getInstance() {
        if (null == restaurantController) {
            restaurantController = new RestaurantController();
        }

        return restaurantController;
    }

    /**
     * <p>
     * Creates all the restaurants.
     * </p>
     *
     * @param restaurants Represents all the {@link Restaurant}
     */
    public boolean loadRestaurants(final Map<Integer, Restaurant> restaurants) {
        return restaurantService.loadRestaurants(restaurants);
    }

    /**
     * <p>
     * Creates the menucard for the restaurant.
     * </p>
     *
     * @param menuCard Contains the list of foods in the restaurant
     */
    public void loadMenuCard(final Map<Food, Restaurant> menuCard) {
        restaurantService.loadMenuCard(menuCard);
    }

    /**
     * <p>
     * Gets all the restaurants.
     * </p>
     *
     * @return The map having all the restaurants
     */
    public Map<Integer, Restaurant> getRestaurants(){
        return restaurantService.getRestaurants();
    }

    /**
     * <p>
     * Gets the available food quantity in the restaurant .
     * </p>
     *
     * @param food Represents the current {@link Food} selected by the user
     * @param quantity Represents the quantity of the food given by the current user
     * @return Available quantity from the selected restaurant
     */
    public int getQuantity(final Food food, final int quantity) {
        return restaurantService.getQuantity(food, quantity);
    }

    /**
     * <p>
     * Gets the menucard of the selected restaurant by the user.
     * </p>
     *
     * @param restaurant Represents the current {@link Restaurant}
     * @return The menucard list
     */
    public List<Food> getMenuCard(final Restaurant restaurant) {
        return restaurantService.getMenuCard(restaurant);
    }
}
