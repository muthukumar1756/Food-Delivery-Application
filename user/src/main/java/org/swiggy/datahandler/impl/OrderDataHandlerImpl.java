package org.swiggy.datahandler.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.swiggy.exception.AddressDataLoadFailureException;
import org.swiggy.exception.OrderDataNotFoundException;
import org.swiggy.exception.OrderPlacementFailureException;
import org.swiggy.datahandler.OrderDataHandler;
import org.swiggy.connection.DataBaseConnection;
import org.swiggy.model.*;

/**
 * <p>
 * Implements the data base service of the order related operation
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class OrderDataHandlerImpl implements OrderDataHandler {

    private static OrderDataHandler orderDataHandler;
    private final Logger logger;
    private final Connection connection;

    private OrderDataHandlerImpl() {
        logger = LogManager.getLogger(OrderDataHandlerImpl.class);
        connection = DataBaseConnection.getConnection();
    }

    /**
     * <p>
     * Gets the object of the order database implementation class.
     * </p>
     *
     * @return The order database service implementation object
     */
    public static OrderDataHandler getInstance(){
        if (null == orderDataHandler) {
            orderDataHandler = new OrderDataHandlerImpl();
        }

        return orderDataHandler;
    }

    /**
     * {@inheritDoc}
     *
     * @param cartList Represents the list of cart items
     * @param orderList Represents the list of orders
     * @return True if the order is placed, false otherwise
     */
    @Override
    public boolean placeOrder(final List<Cart> cartList, List<Order> orderList) {
        updateCartStatus(cartList);
        final String query = """
                insert into orders (user_id, cart_id, address_id) values
                (?, ?, ?)""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (final Order order : orderList) {
                preparedStatement.setLong(1, order.getUser_id());
                preparedStatement.setLong(2, order.getCartId());
                preparedStatement.setLong(3, order.getAddressId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new OrderPlacementFailureException(message.getMessage());
        }

        return true;
    }

    /**
     * <p>
     * Updates the status of the cart.
     * </p>
     *
     * @param cartList Represents the list of cart items
     */
    private void updateCartStatus(final List<Cart> cartList) {
        final String query = "update cart set status = 2 where id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (final Cart cart : cartList) {
                preparedStatement.setLong(1, cart.getId());
                preparedStatement.executeUpdate();
                updateQuantity(cart.getFoodId(), cart.getQuantity());
            }
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new AddressDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param address Represents the address of the user
     */
    @Override
    public void addAddress(final Address address) {
        final String query = """
                insert into address (user_id, house_number, street_name, area_name, city_name, pincode) values
                (?, ?, ?, ?, ?, ?) returning id""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, address.getUserId());
            preparedStatement.setString(2, address.getHouseNumber());
            preparedStatement.setString(3, address.getStreetName());
            preparedStatement.setString(4, address.getAreaName());
            preparedStatement.setString(5, address.getCityName());
            preparedStatement.setString(6, address.getPincode());
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            final int addressId = resultSet.getInt(1);

            address.setId(addressId);
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new AddressDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the current {@link User}
     */
    @Override
    public List<Address> getAddress(final long userId) {
        final String query = "select * from address where user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            final List<Address> addressList = new ArrayList<>();

            while(resultSet.next()) {
                final Address address = new Address();

                address.setId(resultSet.getInt(1));
                address.setUserId(resultSet.getInt(2));
                address.setHouseNumber(resultSet.getString(3));
                address.setStreetName(resultSet.getString(4));
                address.setAreaName(resultSet.getString(5));
                address.setCityName(resultSet.getString(6));
                address.setPincode(resultSet.getString(7));
                addressList.add(address);
            }

            return addressList;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new AddressDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * <p>
     * Updates the food quantity in restaurant after ordered by user.
     * </p>
     *
     * @param foodId Represents the id of the {@link Food}
     * @param quantity quantity Represents the quantity of the food given by the current user
     */
    private void updateQuantity(final long foodId, final int quantity) {
        final String query = "update food set food_quantity = food_quantity - ? where id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setLong(2, foodId);
            preparedStatement.executeUpdate();
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new OrderPlacementFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the current {@link User}
     * @return List having all the orders placed by the user
     */
    @Override
    public List<Order> getOrders(final long userId) {
        final String query = """
                select o.id, f.name, c.quantity, c.total_amount, r.name from orders o
                join cart c on o.cart_id = c.id
                join food f on c.food_id = f.id
                join restaurant r on c.restaurant_id = r.id
                where o.user_id = ? and c.status = 2""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            final List<Order> orderList = new ArrayList<>();

            while (resultSet.next()) {
                final Order order = new Order();

                order.setId(resultSet.getInt(1));
                order.setFoodName(resultSet.getString(2));
                order.setQuantity(resultSet.getInt(3));
                order.setAmount(resultSet.getFloat(4));
                order.setRestaurantName(resultSet.getString(5));
                orderList.add(order);
            }

            return orderList;
        } catch (SQLException message) {
            logger.error(message.getMessage());
            throw new OrderDataNotFoundException(message.getMessage());
        }
    }
}