package org.swiggy.controller;

import org.swiggy.model.Address;
import org.swiggy.model.Cart;
import org.swiggy.model.Order;
import org.swiggy.model.User;
import org.swiggy.service.OrderService;
import org.swiggy.service.impl2.OrderServiceImpl;

import java.util.List;

/**
 * <p>
 * Handles the order related operation and responsible for receiving user input and processing it.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class OrderController {

    private static OrderController orderController;
    private final OrderService orderService;

    private OrderController() {
        orderService = OrderServiceImpl.getInstance();
    }

    /**
     * <p>
     * Gets the object of the order controller class.
     * </p>
     *
     * @return The order controller object
     */
    public static OrderController getInstance() {
        if (null == orderController) {
            orderController = new OrderController();
        }

        return orderController;
    }

    /**
     * <p>
     * places the user orders.
     * </p>
     *
     * @param cartList Represents the list of cart items
     * @param orderList Represents the list of orders
     * @return True if the order is placed, false otherwise
     */
    public boolean placeOrder(final List<Cart> cartList, List<Order> orderList) {
        return orderService.placeOrder(cartList, orderList);
    }

    /**
     * <p>
     * Stores the address of the user.
     * </p>
     *
     * @param address Represents the address of the user
     */
    public void addAddress(final Address address) {
        orderService.addAddress(address);
    }

    /**
     * <p>
     * Displays all the addresses of the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    public List<Address> getAddress(final long userId) {
        return orderService.getAddress(userId);
    }

    /**
     * <p>
     * Gets the orders placed by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @return List having all the orders placed by the user
     */
    public List<Order> getOrders(final long userId) {
        return orderService.getOrders(userId);
    }
}
