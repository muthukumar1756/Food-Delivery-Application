package org.swiggy.exception;

/**
 * <p>
 * Handles the exception when the user cart is not found.
 * </p>
 */
public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(final String message) {
        super(message);
    }
}
