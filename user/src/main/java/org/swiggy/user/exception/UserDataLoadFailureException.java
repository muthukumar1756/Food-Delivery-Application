package org.swiggy.user.exception;

import org.swiggy.exception.UserException;

/**
 * <p>
 * Handles the exception when unable to load user data.
 * </p>
 */
public class UserDataLoadFailureException extends UserException {
    public UserDataLoadFailureException(final String message) {
        super(message);
    }
}