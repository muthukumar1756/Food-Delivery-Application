package org.swiggy.restaurant.service.impl1;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.Restaurant;
import org.swiggy.restaurant.model.RestaurantDataUpdateType;
import org.swiggy.restaurant.service.RestaurantService;

/**
 * <p>
 * Implements the service of the restaurant related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class RestaurantServiceImpl implements RestaurantService {

    private static RestaurantService restaurantService;
    private final List<Restaurant> restaurants = new ArrayList<>();

    private RestaurantServiceImpl() {
    }

    /**
     * <p>
     * Gets the restaurant service implementation object.
     * </p>
     *
     * @return The restaurant service implementation object
     */
    public static RestaurantService getInstance() {
        if (null == restaurantService) {
            restaurantService = new RestaurantServiceImpl();
        }

        return restaurantService;
    }

    @Override
    public boolean createRestaurantProfile(Restaurant restaurant) {
        return false;
    }

    @Override
    public Restaurant getRestaurant(String phoneNumber, String password) {
        return null;
    }

    @Override
    public Restaurant getRestaurantById(long restaurantId) {
        return null;
    }

    @Override
    public boolean addFood(Food food, long restaurantId) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantMap Represents all the {@link Restaurant}
     */
    @Override
    public boolean loadRestaurantsData(final List<Restaurant> restaurantMap) {
        //restaurants.putAll(restaurantMap);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param menuCard Contains the list of foods in the restaurant
     */
    @Override
    public void loadMenuCardData(final Map<Food, Long> menuCard) {
        for (final Map.Entry<Food, Long> restaurantFood : menuCard.entrySet()) {
            final Food food = restaurantFood.getKey();
            final Restaurant restaurant = null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return The map having all the restaurants
     */
    @Override
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    @Override
    public int getQuantity(long foodId) {
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the current {@link Restaurant}
     * @return The menucard list
     */
    @Override
    public List<Food> getMenuCard(final long restaurantId, final int foodType) {
        return null;
    }

    @Override
    public boolean updateRestaurantData(long restaurantId, String restaurantData, RestaurantDataUpdateType type) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param foodId Represents the id of the current {@link Food} selected by the user
     */
    @Override
    public boolean removeFood(final long foodId) {
        return false;
    }
}
