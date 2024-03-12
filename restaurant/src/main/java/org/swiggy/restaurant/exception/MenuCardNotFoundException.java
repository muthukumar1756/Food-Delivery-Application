package org.swiggy.restaurant.exception;

import org.swiggy.exception.RestaurantException;

/**
 * <p>
 * Handles the exception when unable to found the menucard of the selected restaurant.
 * </p>
 */
public class MenuCardNotFoundException extends RestaurantException {
    public MenuCardNotFoundException(final String message) {
        super(message);
    }
}
