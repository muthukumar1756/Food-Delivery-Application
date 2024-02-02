package org.swiggy.exception;

import org.swiggy.RestaurantException;

/**
 * <p>
 * Handles the exception when unable to access the food count.
 * </p>
 */
public class FoodCountAccessException extends RestaurantException {
    public FoodCountAccessException(final String message) {
        super(message);
    }
}
