package org.swiggy.restaurant.service.impl2;

import java.util.List;
import java.util.Map;

import org.swiggy.restaurant.datahandler.RestaurantDataHandler;
import org.swiggy.restaurant.datahandler.impl.RestaurantDataHandlerImpl;
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
     * @return The current restaurant
     */
    @Override
    public Restaurant getRestaurant(final String phoneNumber, final String password) {
        return restaurantDataHandler.getRestaurant(phoneNumber, password);
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     * @return The restaurant object
     */
    public Restaurant getRestaurantById(final long restaurantId) {
        return restaurantDataHandler.getRestaurantById(restaurantId);
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurants Represents list of restaurants
     */
    @Override
    public boolean loadRestaurantsData(final List<Restaurant> restaurants) {
        return restaurantDataHandler.loadRestaurantsData(restaurants);
    }

    /**
     * {@inheritDoc}
     *
     * @param menuCard Contains the list of foods from the restaurant
     */
    public void loadMenuCardData(final Map<Food, Long> menuCard) {
        restaurantDataHandler.loadMenuCardData(menuCard);
    }

    /**
     * {@inheritDoc}
     *
     * @param food Represents the current food added by the restaurant
     * @param restaurantId Represents the id of the Restaurant
     * @return  True if food is added, false otherwise
     */
    @Override
    public boolean addFood(final Food food, final long restaurantId) {
        return restaurantDataHandler.addFood(food, restaurantId);
    }

    /**
     * {@inheritDoc}
     *
     * @return The list of all restaurants
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
     * @return The list of menucard having foods
     */
    @Override
    public List<Food> getMenuCard(final long restaurantId, final int foodTypeId) {
        return restaurantDataHandler.getMenuCard(restaurantId, foodTypeId);
    }

    /**
     * {@inheritDoc}
     *
     * @param foodId Represents the id of the current {@link Food} selected by the user
     * @return True if food is removed, false otherwise
     */
    @Override
    public boolean removeFood(final long foodId) {
        return restaurantDataHandler.removeFood(foodId);
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id 0f the current {@link Restaurant}
     * @param restaurantData Represents the data of the current user to be updated
     * @param type Represents the type of data of the current user to be updated
     * @return True if data is updated, false otherwise
     */
    @Override
    public boolean updateRestaurantData(final long restaurantId, final String restaurantData,
                                     final RestaurantDataUpdateType type) {
        switch (type) {
            case NAME:
                return restaurantDataHandler.updateRestaurantData(restaurantId, "name", restaurantData);
            case PHONENUMBER:
                return restaurantDataHandler.updateRestaurantData(restaurantId, "phone_number", restaurantData);
            case EMAILID :
                return restaurantDataHandler.updateRestaurantData(restaurantId, "email_id", restaurantData);
            case PASSWORD:
                return restaurantDataHandler.updateRestaurantData(restaurantId, "password", restaurantData);
            default:
                throw new IllegalArgumentException("Invalid Restaurant Data UpdateType: " + type);
        }
    }
}
