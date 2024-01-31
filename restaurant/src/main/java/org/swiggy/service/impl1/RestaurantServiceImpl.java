package org.swiggy.service.impl1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.swiggy.model.Food;
import org.swiggy.model.FoodType;
import org.swiggy.model.Restaurant;
import org.swiggy.service.RestaurantService;

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

    public final Map<Integer, Restaurant> restaurants = new HashMap<>();

    private RestaurantServiceImpl () {
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

    /**
     * {@inheritDoc}
     *
     * @param restaurantMap Represents all the {@link Restaurant}
     */
    @Override
    public boolean loadRestaurants(final Map<Integer, Restaurant> restaurantMap) {
        restaurants.putAll(restaurantMap);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param menuCard Contains the list of foods in the restaurant
     */
    public void loadMenuCard(final Map<Food, Restaurant> menuCard) {
        for (final Map.Entry<Food, Restaurant> restaurantFood : menuCard.entrySet()) {
            final Food food = restaurantFood.getKey();
            final Restaurant restaurant = restaurantFood.getValue();

            if (food.getType().equals(FoodType.NONVEG)) {
                restaurant.createNonVegMenuCard(food);
                restaurant.createMenuCard(food);
            } else {
                restaurant.createVegMenuCard(food);
                restaurant.createMenuCard(food);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return The map having all the restaurants
     */
    @Override
    public Map<Integer, Restaurant> getRestaurants(){
        return restaurants;
    }

    /**
     * {@inheritDoc}
     *
     * @param food Represents the current {@link Food} selected by the user
     * @param quantity Represents the quantity of the food given by the current user
     * @return Available quantity from the selected restaurant
     */
    @Override
    public int getQuantity(final Food food, final int quantity) {
        return food.getFoodQuantity();
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurant Represents the current {@link Restaurant}
     * @return The menucard list
     */
    @Override
    public List<Food> getMenuCard(final Restaurant restaurant) {
        return restaurant.getMenuCard();
    }
}
