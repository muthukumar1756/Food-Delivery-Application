package org.swiggy.exception;

/**
 * <p>
 * Handles the exception when the menucard of the selected restaurant is not found.
 * </p>
 */
public class MenuCardNotFoundException extends RuntimeException {
    public MenuCardNotFoundException(final String message) {
        super(message);
    }
}
