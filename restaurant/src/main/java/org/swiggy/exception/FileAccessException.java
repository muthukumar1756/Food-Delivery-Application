package org.swiggy.exception;

/**
 * <p>
 * Handles the exception when the restaurant files cant accessed.
 * </p>
 */
public class FileAccessException extends RuntimeException {
    public FileAccessException(final String message) {
        super(message);
    }
}
