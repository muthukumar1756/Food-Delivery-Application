package org.swiggy.exception;

import org.swiggy.RestaurantException;

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
