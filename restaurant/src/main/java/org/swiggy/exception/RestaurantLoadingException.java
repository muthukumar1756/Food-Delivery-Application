package org.swiggy.exception;

/**
 * <p>
 * Handles the exception when the restaurant cant be created.
 * </p>
 */
public class RestaurantLoadingException extends RuntimeException {
    public RestaurantLoadingException(final String message) {
        super(message);
    }
}