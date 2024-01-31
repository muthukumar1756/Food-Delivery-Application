package org.swiggy;

import org.apache.logging.log4j.LogManager;

import org.swiggy.initializer.RestaurantInitializer;
import org.swiggy.view.UserView;

/**
 * <p>
 * Activates the features of the swiggy application
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class SwiggyActivator {

    /**
     * <p>
     * Starts the execution of swiggy application.
     * </p>
     *
     * @param args Represents command line arguments
     */
    public static void main(final String[] args) {
        RestaurantInitializer.getInstance().loadRestaurants();
        LogManager.getLogger(SwiggyActivator.class).info("Welcome To Swiggy");
        UserView.getInstance().displayMainMenu();
    }
}