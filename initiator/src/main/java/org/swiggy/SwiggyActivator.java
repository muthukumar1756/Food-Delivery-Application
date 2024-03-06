package org.swiggy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.initializer.RestaurantInitializer;
import org.swiggy.view.CommonView;
import org.swiggy.view.RestaurantView;
import org.swiggy.view.UserView;

/**
 * <p>
 * Activates the features of the swiggy application
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class SwiggyActivator extends CommonView {

    private final Logger logger = LogManager.getLogger(SwiggyActivator.class);

    /**
     * <p>
     * Starts the execution of swiggy application.
     * </p>
     *
     */
    public void start() {
        RestaurantInitializer.getInstance().loadRestaurantsData();
        selectRole();
    }

    /**
     * <p>
     * selects the role of the person.
     * </p>
     *
     */
    public void selectRole() {
        logger.info("""
                Welcome To Swiggy
                1.Restaurant
                2.User""");
        switch (getValue()) {
            case 1:
                RestaurantView.getInstance().displayMainMenu();
                break;
            case 2:
                UserView.getInstance().displayMainMenu();
                break;
            default:
                logger.warn("Enter A Valid Option");
                selectRole();
        }
    }
}