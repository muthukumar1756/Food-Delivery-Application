package org.swiggy.exception;

/**
 * <p>
 * Handles the exception of algorithm not found while hashing the user password.
 * </p>
 */
public class HashAlgorithmNotFoundException extends RuntimeException {
    public HashAlgorithmNotFoundException(final String message) {
        super(message);
    }
}
