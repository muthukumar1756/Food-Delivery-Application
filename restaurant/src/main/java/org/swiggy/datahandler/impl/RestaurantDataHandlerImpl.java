package org.swiggy.datahandler.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.exception.CartUpdateException;
import org.swiggy.exception.FoodLoadingException;
import org.swiggy.exception.MenuCardNotFoundException;
import org.swiggy.exception.RestaurantLoadingException;
import org.swiggy.datahandler.RestaurantDataHandler;
import org.swiggy.connection.DataBaseConnection;
import org.swiggy.model.Food;
import org.swiggy.model.FoodType;
import org.swiggy.model.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Implements the data base service of the restaurant related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class RestaurantDataHandlerImpl implements RestaurantDataHandler {

    private static RestaurantDataHandler restaurantDataHandler;

    private final Logger logger;
    private final Connection connection;

    private RestaurantDataHandlerImpl() {
        logger = LogManager.getLogger(RestaurantDataHandlerImpl.class);
        connection = DataBaseConnection.getConnection();
    }

    /**
     * <p>
     * Gets the object of the restaurant database implementation class.
     * </p>
     *
     * @return The restaurant database service implementation object
     */
    public static RestaurantDataHandler getInstance() {
        if (null == restaurantDataHandler) {
            return restaurantDataHandler = new RestaurantDataHandlerImpl();
        }

        return restaurantDataHandler;
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurants Represents all the {@link Restaurant}
     */
    @Override
    public boolean loadRestaurants(final Map<Integer, Restaurant> restaurants) {
        try {
            connection.setAutoCommit(false);

            if (restaurants.size() != getRestaurantsCount()) {
                clearRestaurants();

                for (final Restaurant restaurant : restaurants.values()) {
                    final String query = "insert into restaurant (name) values (?) returning id";
                    final String restaurantName = restaurant.getName();

                    try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, restaurantName);
                        final ResultSet resultSet = preparedStatement.executeQuery();

                        resultSet.next();
                        final int restaurantId = resultSet.getInt(1);

                        restaurant.setRestaurantId(restaurantId);
                    }
                }
                connection.commit();

                return true;
            }
        } catch (SQLException message) {
            try {
                connection.rollback();
            } catch (SQLException exceptionMessage) {
                logger.error(exceptionMessage.getMessage());
                throw new RestaurantLoadingException(exceptionMessage.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException message) {
                logger.error(message.getMessage());
                throw new RestaurantLoadingException(message.getMessage());
            }
        }

        return false;
    }

    /**
     * <p>
     * Checks whether the restaurant has an entry in database or not.
     * </p>
     *
     * @return Count of the restaurant present in the database
     */
    public int getRestaurantsCount() {
        final String query = "select count(*) from restaurant";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            final int result = resultSet.getInt(1);

            return result;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new RestaurantLoadingException(message.getMessage());
        }
    }

    /**
     * <p>
     * Clears all the restaurants.
     * </p>
     */
    private void clearRestaurants() {
        final String query = "truncate table restaurant cascade";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new RestaurantLoadingException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param menuCard Contains the list of foods in the restaurant
     */
    @Override
    public void loadMenuCard(final Map<Food, Restaurant> menuCard) {
        try {
            connection.setAutoCommit(false);

            for (final Map.Entry<Food, Restaurant> restaurantFood : menuCard.entrySet()) {
                final Food food = restaurantFood.getKey();
                final Restaurant restaurant = restaurantFood.getValue();

                final String query = "insert into food(name, rate, food_type, food_quantity) values(?, ?, ?, ?)returning id";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, food.getFoodName());
                    preparedStatement.setFloat(2, food.getRate());
                    preparedStatement.setInt(3, FoodType.getId(food.getType()));
                    preparedStatement.setInt(4, food.getFoodQuantity());
                    final ResultSet resultSet = preparedStatement.executeQuery();

                    resultSet.next();
                    final int foodId = resultSet.getInt(1);

                    food.setFoodId(foodId);
                    loadMenuCardFoods(food, restaurant);
                }
            }
            connection.commit();
        } catch (SQLException message) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                logger.error(message.getMessage());
                throw new FoodLoadingException(exception.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException message) {
                logger.error(message.getMessage());
                throw new FoodLoadingException(message.getMessage());
            }
        }
    }

    /**
     * <p>
     * Maps the food with restaurant.
     * </p>
     *
     * @param food Represents the current {@link Food}
     * @param restaurant Represents the id of the current {@link Restaurant}
     */
    private void loadMenuCardFoods(final Food food, final Restaurant restaurant) {
        final String query = "insert into restaurant_food (food_id, restaurant_id) values(?, ?)";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, food.getFoodId());
            preparedStatement.setInt(2, restaurant.getRestaurantId());
            preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new FoodLoadingException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return The map having all the restaurants.
     */
    @Override
    public Map<Integer, Restaurant> getRestaurants() {
        final String query = "select id, name from restaurant";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            final Map<Integer, Restaurant> restaurants = new HashMap<>();
            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final int id = resultSet.getInt(1);
                final String name = resultSet.getString(2);
                final Restaurant restaurant = new Restaurant(name);

                restaurant.setRestaurantId(id);
                restaurants.put(id, restaurant);
            }

            return restaurants;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new FoodLoadingException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param food Represents the current {@link Food} selected by the user
     * @param quantity Represents the quantity of the food given by the current user
     * @return Available quantity from the selected restaurant
     */
    public int getQuantity(final Food food, final int quantity) {
        final String query = "select food_quantity from food where id = ?";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, food.getFoodId());
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            final int foodQuantity = resultSet.getInt(1);

            return foodQuantity;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new CartUpdateException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurant Represents the current {@link Restaurant}
     * @return The menucard list
     */
    @Override
    public List<Food> getMenuCard(final Restaurant restaurant) {
        final String query = """
        select f.id, f.name, f.rate, f.food_type, f.food_quantity from food f
        join restaurant_food rf on f.id = rf.food_id join restaurant r on rf.restaurant_id = r.id where r.id = ?
        """;

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, restaurant.getRestaurantId());
            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final int id = resultSet.getInt(1);
                final String name = resultSet.getString(2);
                final float rate = resultSet.getFloat(3);
                final int foodType = resultSet.getInt(4);
                final int quantity = resultSet.getInt(5);

                final Food food;
                if (2 == foodType) {
                    food = new Food(name, rate, FoodType.NONVEG, quantity);

                    food.setFoodId(id);
                    restaurant.createNonVegMenuCard(food);
                } else {
                    food = new Food(name, rate, FoodType.VEG, quantity);

                    food.setFoodId(id);
                    restaurant.createVegMenuCard(food);
                }
                restaurant.createMenuCard(food);
            }

            return restaurant.getMenuCard();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new MenuCardNotFoundException(message.getMessage());
        }
    }
}
