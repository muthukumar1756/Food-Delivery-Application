package org.swiggy.exception;

import org.swiggy.CartException;

/**
 * <p>
 * Handles the exception when unable to update the user cart.
 * </p>
 */
public class CartUpdateFailureException extends CartException {
    public CartUpdateFailureException(final String message) {
        super(message);
    }
}
