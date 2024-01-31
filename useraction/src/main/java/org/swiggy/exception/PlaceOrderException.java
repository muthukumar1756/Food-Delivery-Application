package org.swiggy.exception;

/**
 * <p>
 * Handles the exception when the user order cant be placed.
 * </p>
 */
public class PlaceOrderException extends RuntimeException {
    public PlaceOrderException(final String message) {
        super(message);
    }
}
