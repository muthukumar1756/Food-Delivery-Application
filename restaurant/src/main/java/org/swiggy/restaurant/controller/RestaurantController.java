package org.swiggy.restaurant.controller;

import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.Restaurant;
import org.swiggy.restaurant.model.RestaurantDataUpdateType;
import org.swiggy.restaurant.service.RestaurantService;
import org.swiggy.restaurant.service.impl2.RestaurantServiceImpl;

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
     * @return True if restaurant is created, false otherwise
     */
    public boolean createRestaurantProfile(final Restaurant restaurant) {
        return restaurantService.createRestaurantProfile(restaurant);
    }

    /**
     * <p>
     * Gets the restaurant user if the phone_number and password matches.
     * </p>
     *
     * @param phoneNumber Represents the phone_number of the current restaurant user
     * @param password Represents the password of the current restaurant user
     * @return The current restaurant
     */
    public Restaurant getRestaurant(final String phoneNumber, final String password) {
        return restaurantService.getRestaurant(phoneNumber, password);
    }

    /**
     * <p>
     * Gets the restaurant if the id matches.
     * </p>
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     * @return The restaurant object
     */
    public Restaurant getRestaurantById(final long restaurantId) {
        return restaurantService.getRestaurantById(restaurantId);
    }

    /**
     * <p>
     * Loads all the restaurants data.
     * </p>
     *
     * @param restaurants Represents list of restaurants
     */
    public boolean loadRestaurantsData(final List<Restaurant> restaurants) {
        return restaurantService.loadRestaurantsData(restaurants);
    }

    /**
     * <p>
     * Loads the menucard of the restaurant.
     * </p>
     *
     * @param menuCard Contains the list of foods from the restaurant
     */
    public void loadMenuCard(final Map<Food, Long> menuCard) {
        restaurantService.loadMenuCardData(menuCard);
    }

    /**
     * <p>
     * Gets all the restaurants.
     * </p>
     *
     * @return The list of all restaurants
     */
    public List<Restaurant> getRestaurants() {
        return restaurantService.getRestaurants();
    }

    /**
     * <p>
     * Gets the available food quantity in the restaurant.
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
     * Adds food to the restaurant.
     * </p>
     *
     * @param food Represents the current food added by the restaurant
     * @param restaurantId Represents the id of the Restaurant
     */
    public void addFood(final Food food, final long restaurantId) {
        restaurantService.addFood(food, restaurantId);
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
     * @return The list of menucard having foods
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
