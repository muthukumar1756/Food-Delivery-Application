package org.swiggy.exception;

import org.swiggy.CartException;

/**
 * <p>
 * Handles the exception when the user cart is not found.
 * </p>
 */
public class CartDataNotFoundException extends CartException {
    public CartDataNotFoundException(final String message) {
        super(message);
    }
}
