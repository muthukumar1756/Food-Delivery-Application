package org.swiggy.service;

import org.swiggy.model.Food;
import org.swiggy.model.User;

import java.util.Map;

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
    boolean placeOrder(final User user, final Map<Food, Integer> cart);
}
