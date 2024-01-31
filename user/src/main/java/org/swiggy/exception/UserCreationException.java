package org.swiggy.exception;

/**
 * <p>
 * Handles the exception when the user cant be created.
 * </p>
 */
public class UserCreationException extends RuntimeException {
    public UserCreationException(final String message) {
        super(message);
    }
}