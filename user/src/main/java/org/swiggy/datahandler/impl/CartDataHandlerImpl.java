package org.swiggy.datahandler.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.swiggy.exception.CartDataNotFoundException;
import org.swiggy.exception.CartUpdateFailureException;
import org.swiggy.exception.FoodDataLoadFailureException;
import org.swiggy.datahandler.CartDataHandler;
import org.swiggy.connection.DataBaseConnection;
import org.swiggy.exception.RestaurantDataLoadFailureException;
import org.swiggy.model.Food;
import org.swiggy.model.FoodType;
import org.swiggy.model.User;

/**
 * <p>
 * Implements the data base service of the cart related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class CartDataHandlerImpl implements CartDataHandler {

    private static CartDataHandler cartDataHandler;

    private final Logger logger;
    private final Connection connection;

    private CartDataHandlerImpl() {
        logger = LogManager.getLogger(CartDataHandlerImpl.class);
        connection = DataBaseConnection.getConnection();
    }

    /**
     * <p>
     * Gets the object of the cart database implementation class.
     * </p>
     *
     * @return The cart database service implementation object
     */
    public static CartDataHandler getInstance() {
        if (null == cartDataHandler) {
            return cartDataHandler = new CartDataHandlerImpl();
        }

        return cartDataHandler;
    }

    /**
     * {@inheritDoc}
     *
     * @param food Represents the current {@link Food} selected by the user
     * @param user Represents the current {@link User}
     * @param quantity Represents the quantity of the food given by the current user
     * @param restaurantId Represents the id of the current restaurant
     * @return True if the food is added to the user cart, false otherwise
     */
    @Override
    public boolean addFoodToCart(final Food food, final User user, final int quantity, final int restaurantId) {
        try {
            connection.setAutoCommit(false);
            final String query = "insert into user_cart_mapping (food_id, quantity) values (?, ?) returning id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, food.getFoodId());
                preparedStatement.setInt(2, quantity);
                final ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();
                final int cartId = resultSet.getInt(1);

                if (updateCart(user, cartId, restaurantId)) {
                    connection.commit();

                    return true;
                } else {
                    connection.rollback();

                    return false;
                }
            }
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new CartUpdateFailureException(message.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException message) {
                logger.error(message.getMessage());
            }
        }
    }

    /**
     * <p>
     * Maps the cart id with the user cart.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @param cartId Represents the id of the current user cart
     * @param restaurantId Represents the id of the selected restaurant
     * @return True if the food is added to the user cart, false otherwise
     */
    private boolean updateCart(final User user, final int cartId, final int restaurantId) {
        if (isCartEntryExist(user, restaurantId) || isUserCartEmpty(user)) {
            final String query = "insert into user_cart (user_id, restaurant_id, cart_id) values (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, user.getId());
                preparedStatement.setInt(2, restaurantId);
                preparedStatement.setInt(3, cartId);

                return 0 < preparedStatement.executeUpdate();
            } catch (SQLException message) {
                logger.error(message.getMessage());
                throw new FoodDataLoadFailureException(message.getMessage());
            }
        }

        return false;
    }

    /**
     * <p>
     * Checks the user and the restaurant id is already has an entry.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @param restaurantId Represents the id of the selected restaurant
     * @return True if the user entry and restaurant is exist in cart, false otherwise
     */
    private boolean isCartEntryExist(final User user, final int restaurantId) {
        final String query = "select count(*) from user_cart where user_id = ? and restaurant_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, restaurantId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            final int result = resultSet.getInt(1);

            return 0 < result;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * <p>
     * Checks the user has any entry in the cart.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @return True if the user entry is exist in cart, false otherwise
     */
    private boolean isUserCartEmpty(final User user) {
        final String query = "select count(*) from user_cart where user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user.getId());
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            final int result = resultSet.getInt(1);

            return 0 >= result;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param user Represents the current {@link User}
     * @return The map having all the foods from the user cart
     */
    @Override
    public Map<Food, Integer> getCart(final User user) {
        final String query = """
                select f.id, f.name, f.rate, f.food_type, cm.quantity from food f
                join user_cart_mapping cm on f.id = cm.food_id join user_cart uc on cm.id = uc.cart_id
                join users u on uc.user_id = u.id where u.id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user.getId());
            final ResultSet resultSet = preparedStatement.executeQuery();
            final Map<Food, Integer> userCart = new HashMap<>();

            while (resultSet.next()) {
                final int id = resultSet.getInt(1);
                final String name = resultSet.getString(2);
                final float rate = resultSet.getFloat(3);
                final int foodType = resultSet.getInt(4);
                final int quantity = resultSet.getInt(5);

                final Food food;

                if (2 == foodType) {
                    food = new Food(name, rate, FoodType.NONVEG, quantity);
                } else {
                    food = new Food(name, rate, FoodType.VEG, quantity);
                }
                food.setFoodId(id);
                userCart.put(food, quantity);
            }

          return userCart;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new CartDataNotFoundException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param user Represents the current {@link User}
     * @param food Represents the current {@link Food} selected by the user
     * @return True if the food is removed,false otherwise
     */
    @Override
    public boolean removeFood(final User user, final Food food) {
        final String query = """
                delete from user_cart_mapping where id in(
                select cm.id from user_cart_mapping cm join food f on cm.food_id = f.id
                join user_cart uc on cm.id = uc.cart_id join users u on uc.user_id = u.id
                where u.id = ? and f.id = ?)""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, food.getFoodId());

            return 0 < preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new CartUpdateFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param user Represents the current {@link User}
     * @return The true if the cart is cleared, false otherwise
     */
    @Override
    public boolean clearCart(final User user) {
        final String query = "delete from user_cart uc where user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user.getId());

            return 0 < preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new CartUpdateFailureException(message.getMessage());
        }
    }
}
