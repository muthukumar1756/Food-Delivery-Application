package org.swiggy.user.exception;

import org.swiggy.exception.OrderException;

/**
 * <p>
 * Handles the exception when the user order cant be placed.
 * </p>
 */
public class OrderPlacementFailureException extends OrderException {
    public OrderPlacementFailureException(final String message) {
        super(message);
    }
}
