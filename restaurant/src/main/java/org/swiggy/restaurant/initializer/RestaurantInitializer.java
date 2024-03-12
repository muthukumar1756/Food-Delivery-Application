package org.swiggy.restaurant.initializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.restaurant.controller.RestaurantController;
import org.swiggy.restaurant.exception.RestaurantFileAccessException;
import org.swiggy.restaurant.exception.FoodDataLoadFailureException;
import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.FoodType;
import org.swiggy.restaurant.model.Restaurant;

/**
 * <p>
 * Initializes the restaurants and foods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class RestaurantInitializer {

    private static RestaurantInitializer restaurantInitializer;
    private final Logger logger;
    private final RestaurantController restaurantController;

    private RestaurantInitializer() {
        logger = LogManager.getLogger(RestaurantInitializer.class);
        restaurantController = RestaurantController.getInstance();
    }
    
    /**
     * <p>
     * Gets the object of the restaurant initializer class.
     * </p>
     *
     * @return The restaurant initializer object
     */
    public static RestaurantInitializer getInstance() {
        if (null == restaurantInitializer) {
            restaurantInitializer = new RestaurantInitializer();
        }

        return restaurantInitializer;
    }

    /**
     * <p>
     * Loads the data of the restaurant.
     * </p>
     */
    public void loadRestaurantsData() {
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("Restaurants.properties")) {
            final Properties properties = new Properties();
            final List<Restaurant> restaurants = new ArrayList<>();

            properties.load(inputStream);

            for (final Object key : properties.keySet()) {
                final String name = properties.getProperty((String) key);
                final Restaurant restaurant = new Restaurant();

                restaurant.setName(name);
                restaurants.add(restaurant);
            }

            if (restaurantController.loadRestaurantsData(restaurants)) {
                loadMenuCardData(restaurants);
            }
        } catch (IOException message) {
            logger.error(message.getMessage());
            throw new RestaurantFileAccessException(message.getMessage());
        }
    }

    /**
     * <p>
     * Creates food objects from loaded restaurant paths.
     * </p>
     *
     * @param restaurants Represents all the {@link Restaurant}
     */
    private void loadMenuCardData(final List<Restaurant> restaurants) {
        final Map<Food, Long> menuCard = new HashMap<>();

        for (final Restaurant restaurant : restaurants) {
            final String restaurantDataPath = String.join("", restaurant.getName().toLowerCase(),
                    ".properties");

            try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(restaurantDataPath)) {
                final Properties properties = new Properties();

                properties.load(inputStream);

                for (final Object key : properties.keySet()) {
                    final String value = properties.getProperty(String.valueOf(key));
                    final String[] restaurantProperty = value.split(",");
                    final String name = restaurantProperty[0];
                    final int rate = Integer.parseInt(restaurantProperty[1]);
                    final String type = restaurantProperty[2];
                    final int foodQuantity = Integer.parseInt(restaurantProperty[3]);

                    if (type.equalsIgnoreCase(FoodType.VEG.name())) {
                        menuCard.put(new Food(name, rate, FoodType.VEG, foodQuantity), restaurant.getRestaurantId());
                    } else {
                        menuCard.put(new Food(name, rate, FoodType.NONVEG, foodQuantity), restaurant.getRestaurantId());
                    }
                }
            } catch (IOException message) {
                logger.error(message.getMessage());
                throw new FoodDataLoadFailureException(message.getMessage());
            }
        }
        restaurantController.loadMenuCard(menuCard);
    }
}