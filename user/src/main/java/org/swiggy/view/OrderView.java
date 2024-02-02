package org.swiggy.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;

import org.swiggy.controller.OrderController;
import org.swiggy.model.Food;
import org.swiggy.model.Restaurant;
import org.swiggy.model.User;
import org.swiggy.model.Cart;

/**
 * <p>
 * Handles the food orders by the users
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class OrderView extends CommonView {

    private static OrderView orderView;

    private final Logger logger;
    private final CartView cartView;
    private final RestaurantDisplayView restaurantDisplayView;
    private final OrderController orderController;

    private OrderView() {
        logger = LogManager.getLogger(OrderView.class);
        restaurantDisplayView = RestaurantDisplayView.getInstance();
        cartView = CartView.getInstance();
        orderController = OrderController.getInstance();
    }

    /**
     * <p>
     * Gets the object of the order view class.
     * </p>
     *
     * @return The order view object
     */
    public static OrderView getInstance(){
        if (null == orderView) {
            orderView = new OrderView();
        }

        return orderView;
    }

    /**
     * <p>
     * Handles the order placing by the user
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param cart Represents the {@link Cart} of the current user
     * @param user Represents the current {@link User}
     */
    public void placeOrder(final Restaurant restaurant, final Map<Food, Integer> cart, final User user) {
        if (!cart.isEmpty()) {
            final String userAddress = getDeliveryAddress(restaurant, user);

            if (orderController.placeOrder(user, cart)) {
                logger.info(String.format("\nYour Order Is Placed..\nWill Shortly Delivered To This Address : %s", userAddress));
                cartView.displayRestaurantOrLogout(user);
            }
        } else {
            handleEmptyCart(restaurant, cart, user);
        }
    }

    /**
     * <p>
     * Gets the delivery address of the current user.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param user Represents the current {@link User}
     * @return The address of the current user
     */
    private String getDeliveryAddress(final Restaurant restaurant, final User user) {
        logger.info("Enter Your Address");
        logger.info("Enter Your Door Number");
        final String doorNum = getInfo();

        logger.info("Enter Your Street Name");
        final String streetName = getInfo();

        logger.info("Enter Your Area Name");
        final String areaName = getInfo();

        logger.info("Enter Your City Name");
        final String cityName = getInfo();

        final String userAddress = String.join(" ", doorNum, streetName, areaName, cityName);
        user.setAddress(userAddress);

        if ("back".equals(doorNum) || "back".equals(streetName) || "back".equals(areaName) ||
                "back".equals(cityName)) {
            cartView.displayCart(restaurant, user);
        }

        return userAddress;
    }

    /**
     * <p>
     * Handles the user cart if it has no foods.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param cart Represents the {@link Cart} of the current user
     * @param user Represents the current {@link User}
     */
    private void handleEmptyCart(final Restaurant restaurant, final Map<Food, Integer> cart, final User user) {
        logger.info("Your Order Is Empty\nPlease Select A option From Below:\n1.To Order Foods\n2.Logout");
        final int userChoice = getChoice();

        if (- 1 == userChoice) {
            restaurantDisplayView.addFoodOrPlaceOrder(restaurant, user);
        }

        switch (userChoice) {
            case 1:
                restaurantDisplayView.displayRestaurants(user);
                break;
            case 2:
                logger.info("Your Account Is Logged Out");
                UserView.getInstance().displayMainMenu();
                break;
            default:
                logger.warn("Enter A Valid Option");
                placeOrder(restaurant, cart, user);
        }
    }
}
