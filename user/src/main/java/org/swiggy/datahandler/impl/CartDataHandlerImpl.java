package org.swiggy.datahandler.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.swiggy.exception.CartDataNotFoundException;
import org.swiggy.exception.CartUpdateFailureException;
import org.swiggy.datahandler.CartDataHandler;
import org.swiggy.connection.DataBaseConnection;
import org.swiggy.exception.RestaurantDataLoadFailureException;
import org.swiggy.model.Cart;
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
     * @param cart Represents the cart of the user
     * @return True if the food is added to the user cart, false otherwise
     */
    @Override
    public boolean addFoodToCart(final Cart cart) {

        if (isCartEntryExist(cart.getUserId(), cart.getRestaurantId()) || isUserCartEmpty(cart.getUserId())) {
            final String query = """
            insert into cart (user_id, restaurant_id, food_id, quantity, total_amount) values
            (?, ?, ?, ?, ?) returning id""";

            try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, cart.getUserId());
                preparedStatement.setLong(2, cart.getRestaurantId());
                preparedStatement.setLong(3, cart.getFoodId());
                preparedStatement.setInt(4, cart.getQuantity());
                preparedStatement.setFloat(5, cart.getAmount());
                final ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();
                final int cartId = resultSet.getInt(1);

                cart.setId(cartId);

                return true;
            } catch (SQLException message) {
                logger.error(message.getMessage());
                throw new CartUpdateFailureException(message.getMessage());
            }
        }

        return false;
    }

    /**
     * <p>
     * Checks the user and the restaurant id is already has an entry.
     * </p>
     *
     * @param userId Represents the id 0f the current {@link User}
     * @param restaurantId Represents the id of the selected restaurant
     * @return True if the user entry and restaurant is exist in cart, false otherwise
     */
    private boolean isCartEntryExist(final long userId, final long restaurantId) {
        final String query = "select count(*) from cart where user_id = ? and restaurant_id = ? and status = 1";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, restaurantId);
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
     * @param userId Represents the id 0f the current {@link User}
     * @return True if the user entry is exist in cart, false otherwise
     */
    private boolean isUserCartEmpty(final long userId) {
        final String query = "select count(*) from cart where user_id = ? and status = 1";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
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
     * @param userId Represents the id 0f the current {@link User}
     * @return The map having all the foods from the user cart
     */
    @Override
    public List<Cart> getCart(final long userId) {
        final String query = """
                select c.id, f.name, r.name, c.quantity, c.total_amount from food f
                join cart c on f.id = c.food_id
                join restaurant r on c.restaurant_id = r.id
                join users u on c.user_id = u.id where u.id = ? and c.status = 1""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            final List<Cart> cartList = new ArrayList<>();

            while (resultSet.next()) {
                final Cart cart = new Cart();

                cart.setId(resultSet.getLong(1));
                cart.setFoodName(resultSet.getString(2));
                cart.setRestaurantName(resultSet.getString(3));
                cart.setQuantity(resultSet.getInt(4));
                cart.setAmount(resultSet.getFloat(5));
                cartList.add(cart);
            }

          return cartList;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new CartDataNotFoundException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param cartId Represents the id 0f the user cart
     * @return True if the food is removed,false otherwise
     */
    @Override
    public boolean removeFood(final long cartId) {
        final String query = "delete from cart c where c.id = ? and status = 1";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, cartId);

            return 0 < preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new CartUpdateFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id 0f the current {@link User}
     * @return The true if the cart is cleared, false otherwise
     */
    @Override
    public boolean clearCart(final long userId) {
        final String query = "delete from cart where user_id = ? and status = 1";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);

            return 0 < preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new CartUpdateFailureException(message.getMessage());
        }
    }
}
