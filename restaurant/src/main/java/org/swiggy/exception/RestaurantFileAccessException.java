package org.swiggy.exception;

import org.swiggy.RestaurantException;

/**
 * <p>
 * Handles the exception when unable to access restaurant file.
 * </p>
 */
public class RestaurantFileAccessException extends RestaurantException {
    public RestaurantFileAccessException(final String message) {
        super(message);
    }
}
