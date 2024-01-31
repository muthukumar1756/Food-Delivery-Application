package org.swiggy.service.impl2;

import java.util.List;
import java.util.Map;

import org.swiggy.datahandler.RestaurantDataHandler;
import org.swiggy.datahandler.impl.RestaurantDataHandlerImpl;
import org.swiggy.model.Food;
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

    private final RestaurantDataHandler restaurantDataHandler;

    private RestaurantServiceImpl() {
        restaurantDataHandler = RestaurantDataHandlerImpl.getInstance();
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
     * @param restaurants Represents all the {@link Restaurant}
     */
    @Override
    public boolean loadRestaurants(final Map<Integer, Restaurant> restaurants) {
        return restaurantDataHandler.loadRestaurants(restaurants);
    }

    /**
     * {@inheritDoc}
     *
     * @param menuCard Contains the list of foods in the restaurant
     */
    public void loadMenuCard(final Map<Food, Restaurant> menuCard) {
        restaurantDataHandler.loadMenuCard(menuCard);
    }

    /**
     * {@inheritDoc}
     *
     * @return The map having all the restaurants
     */
    @Override
    public Map<Integer, Restaurant> getRestaurants() {
        return restaurantDataHandler.getRestaurants();
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
        return restaurantDataHandler.getQuantity(food, quantity);
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurant Represents the current {@link Restaurant}
     * @return The menucard list
     */
    @Override
    public List<Food> getMenuCard(final Restaurant restaurant) {
        return restaurantDataHandler.getMenuCard(restaurant);
    }
}
