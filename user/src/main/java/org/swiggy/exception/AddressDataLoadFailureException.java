package org.swiggy.exception;

import org.swiggy.UserException;

/**
 * <p>
 * Handles the exception when the address cant uploaded.
 * </p>
 */
public class AddressDataLoadFailureException extends UserException {
    public AddressDataLoadFailureException(final String message) {
        super(message);
    }
}
