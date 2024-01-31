package org.swiggy.exception;

/**
 * <p>
 * Handles the exception when the user data cant be updated.
 * </p>
 */
public class UserUpdateException extends RuntimeException {
    public UserUpdateException(final String message) {
        super(message);
    }
}
