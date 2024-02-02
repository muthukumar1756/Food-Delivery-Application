package org.swiggy.initializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.controller.RestaurantController;
import org.swiggy.exception.RestaurantFileAccessException;
import org.swiggy.exception.FoodDataLoadFailureException;
import org.swiggy.model.Food;
import org.swiggy.model.FoodType;
import org.swiggy.model.Restaurant;

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
    public void loadRestaurants() {
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("Restaurants.properties")) {
            final Properties properties = new Properties();
            final Map<Integer, Restaurant> restaurants = new HashMap<>();

            properties.load(inputStream);

            for (final Object key : properties.keySet()) {
                final int id = Integer.parseInt((String) key);
                final String name = properties.getProperty((String) key);
                final Restaurant restaurant = new Restaurant(name);

                restaurants.put(id, restaurant);
            }

            if (restaurantController.loadRestaurants(restaurants)) {
                loadMenuCard(restaurants);
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
    private void loadMenuCard(final Map<Integer, Restaurant> restaurants) {
        final Map<Food, Restaurant> menuCard = new HashMap<>();

        for (final Restaurant restaurant : restaurants.values()) {
            final String restaurantPath = String.join("", restaurant.getName().toLowerCase(), ".properties");

            try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(restaurantPath)) {
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
                        menuCard.put(new Food(name, rate, FoodType.VEG, foodQuantity), restaurant);
                    } else {
                        menuCard.put(new Food(name, rate, FoodType.NONVEG, foodQuantity), restaurant);
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
