package org.swiggy.service;

import org.swiggy.model.Address;
import org.swiggy.model.Cart;
import org.swiggy.model.Order;
import org.swiggy.model.User;

import java.util.List;

/**
 * <p>
 * Provides the services for the user orders.
 * </p>
 *
 * @author Muthu kumar v
 * @version 1.0
 */
public interface OrderService {

    /**
     * <p>
     * places the user orders.
     * </p>
     *
     * @param user Represents the current {@link User}
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
