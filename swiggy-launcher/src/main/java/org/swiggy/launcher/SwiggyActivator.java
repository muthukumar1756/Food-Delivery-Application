package org.swiggy.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.swiggy.restaurant.initializer.RestaurantInitializer;
import org.swiggy.common.view.CommonView;
import org.swiggy.common.view.CommonViewImpl;
import org.swiggy.restaurant.view.RestaurantView;
import org.swiggy.user.view.UserView;

/**
 * <p>
 * Activates the features of the swiggy application
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class SwiggyActivator implements BundleActivator {

    private final Logger logger = LogManager.getLogger(SwiggyActivator.class);
    private final CommonView commonView = CommonViewImpl.getInstance();

    @Override
    public void start(final BundleContext context) {
        System.out.println("Initiator Bundle Is Started");
    }

    /**
     * <p>
     * Starts the execution of swiggy application.
     * </p>
     *
     */
    public void launch() {
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
        switch (commonView.getValue()) {
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

    @Override
    public void stop(final BundleContext context) {
        System.out.println("Initiator Bundle Is Stopped");
    }
}