package org.swiggy.datahandler;

import org.swiggy.model.Food;
import org.swiggy.model.User;

import java.util.Map;

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
     * @param user Represents the current {@link User}
     * @return True if the order is placed, false otherwise
     */
    boolean placeOrder(final User user, final Map<Food, Integer> cart);
}
