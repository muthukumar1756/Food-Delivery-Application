package org.swiggy.initializer;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.controller.RestaurantController;
import org.swiggy.exception.FileAccessException;
import org.swiggy.exception.FoodLoadingException;
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
    private final String restaurantsPath;

    private RestaurantInitializer() {
        logger = LogManager.getLogger(RestaurantInitializer.class);
        restaurantController = RestaurantController.getInstance();
        System.setProperty("RESTAURANTPATH", "D:\\java\\Projects java\\FoodDeliveryApp\\Restaurants\\");
        restaurantsPath = System.getProperty("RESTAURANTPATH");
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
        final String path = String.join("", restaurantsPath, "Restaurants.properties");

        try (final FileReader fileReader = new FileReader(path)) {
            final Properties properties = new Properties();
            final Map<Integer, Restaurant> restaurants = new HashMap<>();

            properties.load(fileReader);

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
            throw new FileAccessException(message.getMessage());
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
            final String restaurantPath = String.join("", restaurantsPath,
                    restaurant.getName().toLowerCase(), ".properties");

            try (final FileReader fileReader = new FileReader(restaurantPath)) {
                final Properties properties = new Properties();

                properties.load(fileReader);

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
                throw new FoodLoadingException(message.getMessage());
            }
        }
        restaurantController.loadMenuCard(menuCard);
    }
}
