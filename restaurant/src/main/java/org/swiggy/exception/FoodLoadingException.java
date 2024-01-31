package org.swiggy.exception;

/**
 * <p>
 * Handles the exception when the food cant be created.
 * </p>
 */
public class FoodLoadingException extends RuntimeException {
    public FoodLoadingException(final String message) {
        super(message);
    }
}
