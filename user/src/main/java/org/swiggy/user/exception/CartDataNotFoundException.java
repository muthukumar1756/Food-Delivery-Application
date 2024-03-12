package org.swiggy.user.exception;

import org.swiggy.exception.CartException;

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
