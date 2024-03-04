package org.swiggy.datahandler;

import org.swiggy.model.Address;
import org.swiggy.model.Cart;
import org.swiggy.model.Order;
import org.swiggy.model.User;

import java.util.List;

/**
 * <p>
 * Provides data base service for the user order.
 * </p>
 *
 * @author Muthu kumar v
 * @version 1.1
 */
public interface OrderDataHandler {

    /**
     * <p>
     * places the user orders.
     * </p>
     *
     * @param cartList Represents the list of cart items of user
     * @param orderList Represents the list of orders placed by user
     * @return True if the order is placed, false otherwise
     */
    boolean placeOrder(final List<Cart> cartList, List<Order> orderList);

    /**
     * <p>
     * Stores the address of the user.
     * </p>
     *
     * @param address Represents the address of the user
     */
    void addAddress(final Address address);

    /**
     * <p>
     * Displays all the addresses of the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    List<Address> getAddress(final long userId);

    /**
     * <p>
     * Gets the orders placed by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @return List having all the orders placed by the user
     */
    List<Order> getOrders(final long userId);
}
