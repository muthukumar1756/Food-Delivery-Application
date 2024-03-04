package org.swiggy.service.impl2;

import java.util.List;
import java.util.Map;

import org.swiggy.datahandler.RestaurantDataHandler;
import org.swiggy.datahandler.impl.RestaurantDataHandlerImpl;
import org.swiggy.model.Food;
import org.swiggy.model.Restaurant;
import org.swiggy.model.RestaurantDataUpdateType;
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
     * @param restaurant Represents the current {@link Restaurant}
     * @return True if user is created, false otherwise
     */
    @Override
    public boolean createRestaurantProfile(final Restaurant restaurant) {
        return restaurantDataHandler.createRestaurantProfile(restaurant);
    }

    /**
     * {@inheritDoc}
     *
     * @param phoneNumber Represents the phone_number of the current restaurant user
     * @param password Represents the password of the current restaurant user
     * @return The current user
     */
    @Override
    public Restaurant getRestaurant(final String phoneNumber, final String password) {
        return restaurantDataHandler.getRestaurant(phoneNumber, password);
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     * @return The current restaurant user
     */
    public Restaurant getRestaurantById(final long restaurantId) {
        return restaurantDataHandler.getRestaurantById(restaurantId);
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurants Represents all the {@link Restaurant}
     */
    @Override
    public boolean loadRestaurantsData(final List<Restaurant> restaurants) {
        return restaurantDataHandler.loadRestaurantsData(restaurants);
    }

    /**
     * {@inheritDoc}
     *
     * @param food Represents the current food added by the restaurant
     * @param restaurantId Represents the id of the Restaurant
     * @return  True if food is added, false otherwise
     */
    @Override
    public void loadFoodData(final Food food, final long restaurantId) {
        restaurantDataHandler.loadFoodData(food, restaurantId);
    }

    /**
     * {@inheritDoc}
     *
     * @param menuCard Contains the list of foods in the restaurant
     */
    public void loadMenuCardData(final Map<Food, Integer> menuCard) {
        restaurantDataHandler.loadMenuCardData(menuCard);
    }

    /**
     * {@inheritDoc}
     *
     * @return The map having all the restaurants
     */
    @Override
    public List<Restaurant> getRestaurants() {
        return restaurantDataHandler.getRestaurants();
    }

    /**
     * {@inheritDoc}
     *
     * @param foodId Represents the id of the current {@link Food} selected by the user
     * @return Available quantity from the selected restaurant
     */
    @Override
    public int getQuantity(final long foodId) {
        return restaurantDataHandler.getQuantity(foodId);
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the Restaurant
     * @param foodTypeId Represents the id of the food type.
     * @return The menucard list
     */
    @Override
    public List<Food> getMenuCard(final long restaurantId, final int foodTypeId) {
        return restaurantDataHandler.getMenuCard(restaurantId, foodTypeId);
    }

    /**
     * {@inheritDoc}
     *
     * @param foodId Represents the id of the current {@link Food} selected by the user
     */
    @Override
    public void removeFood(final long foodId) {
        restaurantDataHandler.removeFood(foodId);
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of current {@link Restaurant}
     * @param type Represents the restaurant user data type to be updated
     */
    @Override
    public void updateRestaurantData(final long restaurantId, final String restaurantData,
                                     final RestaurantDataUpdateType type) {
        switch (type) {
            case NAME:
                restaurantDataHandler.updateRestaurantData(restaurantId, "name", restaurantData);
                break;
            case PHONENUMBER:
                restaurantDataHandler.updateRestaurantData(restaurantId, "phone_number", restaurantData);
                break;
            case EMAILID :
                restaurantDataHandler.updateRestaurantData(restaurantId, "email_id", restaurantData);
                break;
            case PASSWORD:
                restaurantDataHandler.updateRestaurantData(restaurantId, "password", restaurantData);
                break;
        }
    }
}
