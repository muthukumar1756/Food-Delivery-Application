package org.swiggy.controller;

import org.swiggy.model.Food;
import org.swiggy.model.Restaurant;
import org.swiggy.model.RestaurantDataUpdateType;
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
     * Creates the new user.
     * </p>
     *
     * @param restaurant Represents the current {@link Restaurant}
     * @return True if user is created, false otherwise
     */
    public boolean createRestaurantProfile(final Restaurant restaurant) {
        return restaurantService.createRestaurantProfile(restaurant);
    }

    /**
     * <p>
     * Gets the user if the phone_number and password matches.
     * </p>
     *
     * @param phoneNumber Represents the phone_number of the current restaurant user
     * @param password Represents the password of the current restaurant user
     * @return The current user
     */
    public Restaurant getRestaurant(final String phoneNumber, final String password) {
        return restaurantService.getRestaurant(phoneNumber, password);
    }

    /**
     * <p>
     * Gets the user if the id matches.
     * </p>
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     * @return The current restaurant user
     */
    public Restaurant getRestaurantById(final long restaurantId) {
        return restaurantService.getRestaurantById(restaurantId);
    }

    /**
     * <p>
     * Creates all the restaurants.
     * </p>
     *
     * @param restaurants Represents all the {@link Restaurant}
     */
    public boolean loadRestaurantsData(final List<Restaurant> restaurants) {
        return restaurantService.loadRestaurantsData(restaurants);
    }

    /**
     * {@inheritDoc}
     *
     * @param food Represents the current food added by the restaurant
     * @param restaurantId Represents the id of the Restaurant
     * @return  True if food is added, false otherwise
     */
    public void loadFoodData(final Food food, final long restaurantId) {
        restaurantService.loadFoodData(food, restaurantId);
    }

    /**
     * <p>
     * Creates the menucard for the restaurant.
     * </p>
     *
     * @param menuCard Contains the list of foods in the restaurant
     */
    public void loadMenuCardData(final Map<Food, Integer> menuCard) {
        restaurantService.loadMenuCardData(menuCard);
    }

    /**
     * <p>
     * Gets all the restaurants.
     * </p>
     *
     * @return The map having all the restaurants
     */
    public List<Restaurant> getRestaurants() {
        return restaurantService.getRestaurants();
    }

    /**
     * <p>
     * Gets the available food quantity in the restaurant .
     * </p>
     *
     * @param foodId Represents the id of the current {@link Food} selected by the user
     * @return Available quantity from the selected restaurant
     */
    public int getQuantity(final long foodId) {
        return restaurantService.getQuantity(foodId);
    }

    /**
     * <p>
     * Removes the food from the restaurant.
     * </p>
     *
     * @param foodId Represents the id of the current {@link Food} selected by the user
     */
    public void removeFood(final long foodId) {
        restaurantService.removeFood(foodId);
    }

    /**
     * <p>
     * Gets the menucard of the selected restaurant by the user.
     * </p>
     *
     * @param restaurantId Represents the id of the Restaurant
     * @param foodTypeId Represents the id of the food type.
     * @return The menucard list
     */
    public List<Food> getMenuCard(final long restaurantId, final int foodTypeId) {
        return restaurantService.getMenuCard(restaurantId, foodTypeId);
    }

    /**
     * <p>
     * Updates the data of the current restaurant user.
     * </p>
     *
     * @param restaurantId Represents the id 0f the current {@link Restaurant}
     * @param restaurantData Represents the data of the current user to be updated
     * @param type Represents the type of data of the current user to be updated
     */
    public void updateRestaurantData(final long restaurantId, final String restaurantData,
                                     final RestaurantDataUpdateType type) {
        restaurantService.updateRestaurantData(restaurantId, restaurantData, type);
    }
}
