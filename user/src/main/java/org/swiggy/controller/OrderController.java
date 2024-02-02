package org.swiggy.controller;

import org.swiggy.model.Food;
import org.swiggy.model.User;
import org.swiggy.service.OrderService;
import org.swiggy.service.impl2.OrderServiceImpl;

import java.util.Map;

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
     * @param user Represents the current {@link User}
     * @return True if the order is placed, false otherwise
     */
    public boolean placeOrder(final User user, final Map<Food, Integer> cart) {
        return orderService.placeOrder(user, cart);
    }
}
