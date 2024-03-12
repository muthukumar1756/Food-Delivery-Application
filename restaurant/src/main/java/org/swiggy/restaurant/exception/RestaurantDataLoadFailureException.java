package org.swiggy.restaurant.exception;

import org.swiggy.exception.RestaurantException;

/**
 * <p>
 * Handles the exception when unable to load the restaurant.
 * </p>
 */
public class RestaurantDataLoadFailureException extends RestaurantException {
    public RestaurantDataLoadFailureException(final String message) {
        super(message);
    }
}