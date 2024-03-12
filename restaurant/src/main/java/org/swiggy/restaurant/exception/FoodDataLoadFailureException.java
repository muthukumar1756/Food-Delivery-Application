package org.swiggy.restaurant.exception;

import org.swiggy.exception.RestaurantException;

/**
 * <p>
 * Handles the exception when unable to load the food.
 * </p>
 */
public class FoodDataLoadFailureException extends RestaurantException {
    public FoodDataLoadFailureException(final String message) {
        super(message);
    }
}
