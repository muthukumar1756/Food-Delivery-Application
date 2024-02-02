package org.swiggy.exception;

import org.swiggy.UserException;

/**
 * <p>
 * Handles the exception when the user data is not found.
 * </p>
 */
public class UserDataNotFoundException extends UserException {
    public UserDataNotFoundException(final String message) {
        super(message);
    }
}