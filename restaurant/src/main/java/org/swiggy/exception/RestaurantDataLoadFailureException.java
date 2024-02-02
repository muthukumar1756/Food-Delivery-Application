package org.swiggy.exception;

import org.swiggy.RestaurantException;

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