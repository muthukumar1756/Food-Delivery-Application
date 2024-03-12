package org.swiggy.common.exception;

import org.swiggy.exception.UserException;

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
