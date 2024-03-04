package org.swiggy.datahandler.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.exception.FoodDataLoadFailureException;
import org.swiggy.exception.MenuCardNotFoundException;
import org.swiggy.exception.FoodCountAccessException;
import org.swiggy.exception.RestaurantDataLoadFailureException;
import org.swiggy.datahandler.RestaurantDataHandler;
import org.swiggy.connection.DataBaseConnection;
import org.swiggy.model.Food;
import org.swiggy.model.FoodType;
import org.swiggy.model.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
     * @param restaurant Represents the current {@link Restaurant}
     * @return True if user is created, false otherwise
     */
    @Override
    public boolean createRestaurantProfile(final Restaurant restaurant) {
        final String query = """
                insert into restaurant (name, phone_number, email_id, password) values (?, ?, ?, ?) returning id""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, restaurant.getName());
            preparedStatement.setString(2, restaurant.getPhoneNumber());
            preparedStatement.setString(3, restaurant.getEmailId());
            preparedStatement.setString(4, restaurant.getPassword());
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            restaurant.setRestaurantId(resultSet.getInt(1));

            return true;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * <p>
     * Gets the restaurant if the phone_number and password matches.
     * </p>
     *
     * @param phoneNumber Represents the phone_number of the current restaurant user
     * @param password Represents the password of the current restaurant user
     * @return The current user
     */
    public Restaurant getRestaurant(final String phoneNumber, final String password) {
        final String query = """
                select id, name, phone_number, email_id, password from restaurant where phone_Number = ? and
                password = ?""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, password);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                final Restaurant restaurant = new Restaurant();

                restaurant.setRestaurantId(resultSet.getInt(1));
                restaurant.setName(resultSet.getString(2));
                restaurant.setPhoneNumber(resultSet.getString(3));
                restaurant.setEmailId(resultSet.getString(4));
                restaurant.setPassword(resultSet.getString(5));

                return restaurant;
            } else {
                return null;
            }
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     * @return The current restaurant user
     */
    @Override
    public Restaurant getRestaurantById(final long restaurantId) {
        final String query = """
                select id, name, phone_number, email_id, password from restaurant where id = ?""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, restaurantId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                final Restaurant restaurant = new Restaurant();

                restaurant.setRestaurantId(resultSet.getInt(1));
                restaurant.setName(resultSet.getString(2));
                restaurant.setPhoneNumber(resultSet.getString(3));
                restaurant.setEmailId(resultSet.getString(4));
                restaurant.setPassword(resultSet.getString(5));

                return restaurant;
            } else {
                return null;
            }
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     * @return True if user is created, false otherwise
     */
    @Override
    public void loadFoodData(final Food food, final long restaurantId) {
        try {
            connection.setAutoCommit(false);
            final String query = """
                    insert into food (name, rate, food_type, food_quantity) values(?, ?, ?, ?) returning id""";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, food.getFoodName());
                preparedStatement.setFloat(2, food.getRate());
                preparedStatement.setInt(3, FoodType.getId(food.getType()));
                preparedStatement.setInt(4, food.getFoodQuantity());
                final ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();

                food.setFoodId(resultSet.getInt(1));
                loadMenuCardFoods(food.getFoodId(), restaurantId);
            }
            connection.commit();
        } catch (SQLException message) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                logger.error(message.getMessage());
                throw new FoodDataLoadFailureException(exception.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException message) {
                logger.error(message.getMessage());
                throw new FoodDataLoadFailureException(message.getMessage());
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurants Represents all the {@link Restaurant}
     */
    @Override
    public boolean loadRestaurantsData(final List<Restaurant> restaurants) {
        try {
            connection.setAutoCommit(false);

            if (restaurants.size() != getRestaurantsCount()) {
                clearRestaurants();

                for (final Restaurant restaurant : restaurants) {
                    final String query = "insert into restaurant (name) values (?) returning id";
                    final String restaurantName = restaurant.getName();

                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
                throw new RestaurantDataLoadFailureException(exceptionMessage.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException message) {
                logger.error(message.getMessage());
                throw new RestaurantDataLoadFailureException(message.getMessage());
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

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return resultSet.getInt(1);
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * <p>
     * Clears all the restaurants.
     * </p>
     */
    private void clearRestaurants() {
        final String query = "truncate table restaurant cascade";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param menuCard Contains the list of foods in the restaurant
     */
    @Override
    public void loadMenuCardData(final Map<Food, Integer> menuCard) {
        try {
            connection.setAutoCommit(false);

            for (final Map.Entry<Food, Integer> restaurantFood : menuCard.entrySet()) {
                final Food food = restaurantFood.getKey();
                final int restaurantId = restaurantFood.getValue();

                final String query = """
                insert into food(name, rate, food_type, food_quantity) values(?, ?, ?, ?) returning id""";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, food.getFoodName());
                    preparedStatement.setFloat(2, food.getRate());
                    preparedStatement.setInt(3, FoodType.getId(food.getType()));
                    preparedStatement.setInt(4, food.getFoodQuantity());
                    final ResultSet resultSet = preparedStatement.executeQuery();

                    resultSet.next();
                    final int foodId = resultSet.getInt(1);

                    food.setFoodId(foodId);
                    loadMenuCardFoods(food.getFoodId(), restaurantId);
                }
            }
            connection.commit();
        } catch (SQLException message) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                logger.error(message.getMessage());
                throw new FoodDataLoadFailureException(exception.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException message) {
                logger.error(message.getMessage());
                throw new FoodDataLoadFailureException(message.getMessage());
            }
        }
    }

    /**
     * <p>
     * Maps the food with restaurant.
     * </p>
     *
     * @param foodId Represents the id of the current {@link Food}
     * @param restaurantId Represents the id of the current {@link Restaurant}
     */
    private void loadMenuCardFoods(final long foodId, final long restaurantId) {
        final String query = "insert into restaurant_food (food_id, restaurant_id) values(?, ?)";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, foodId);
            preparedStatement.setLong(2, restaurantId);
            preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new FoodDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return The map having all the restaurants.
     */
    @Override
    public List<Restaurant> getRestaurants() {
        final String query = "select id, name from restaurant";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            final List<Restaurant> restaurants = new ArrayList<>();
            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final int id = resultSet.getInt(1);
                final Restaurant restaurant = new Restaurant();

                restaurant.setRestaurantId(id);
                restaurant.setName(resultSet.getString(2));
                restaurants.add(restaurant);
            }

            return restaurants;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new FoodDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param foodId Represents the id of the current {@link Food} selected by the user
     * @return Available quantity from the selected restaurant
     */
    public int getQuantity(final long foodId) {
        final String query = "select food_quantity from food where id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, foodId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return resultSet.getInt(1);
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new FoodCountAccessException(message.getMessage());
        }
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
        final String query = """
                select f.id, f.name, f.rate, f.food_type, f.food_quantity from food f
                join restaurant_food rf on f.id = rf.food_id
                join restaurant r on rf.restaurant_id = r.id where r.id = ? and f.food_type in (?, ?)""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, restaurantId);

            if (1 == foodTypeId) {
                preparedStatement.setInt(2, 1);
                preparedStatement.setInt(3, -1);
            } else if (2 == foodTypeId) {
                preparedStatement.setInt(2, 2);
                preparedStatement.setInt(3, -1);
            } else {
                preparedStatement.setInt(2, 1);
                preparedStatement.setInt(3, 2);
            }

            final ResultSet resultSet = preparedStatement.executeQuery();
            final List<Food> menucard = new ArrayList<>();

            while (resultSet.next()) {
                final Food food = new Food();

                food.setFoodId(resultSet.getInt(1));
                food.setFoodName(resultSet.getString(2));
                food.setFoodRate(resultSet.getFloat(3));
                food.setFoodType(FoodType.getTypeById(resultSet.getInt(4)));
                food.setFoodQuantity(resultSet.getInt(5));
                menucard.add(food);
            }

            return menucard;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new MenuCardNotFoundException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param foodId Represents the id of the current {@link Food} selected by the user
     */
    @Override
    public void removeFood(final long foodId) {
        final String query = "delete from food where id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, foodId);
            preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new FoodDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id 0f the current {@link Restaurant}
     * @param restaurantData Represents the data of the current user to be updated
     * @param type Represents the type of data of the current user to be updated
     */
    @Override
    public void updateRestaurantData(final long restaurantId, final String type, final String restaurantData) {
        final String query = String.join("", "update restaurant set ", type, " = ? where id = ?");

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, restaurantData);
            preparedStatement.setLong(2, restaurantId);
            preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }
    }
}