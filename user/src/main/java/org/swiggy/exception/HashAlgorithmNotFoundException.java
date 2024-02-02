package org.swiggy.exception;

import org.swiggy.UserException;

/**
 * <p>
 * Handles the exception of algorithm not found while hashing the user password.
 * </p>
 */
public class HashAlgorithmNotFoundException extends UserException {
    public HashAlgorithmNotFoundException(final String message) {
        super(message);
    }
}
