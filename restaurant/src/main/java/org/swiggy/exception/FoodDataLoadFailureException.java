package org.swiggy.exception;

import org.swiggy.RestaurantException;

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
