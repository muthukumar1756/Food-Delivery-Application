package org.swiggy.exception;

public class RestaurantException extends RuntimeException {
    public RestaurantException(final String message) {
        super(message);
    }
}
