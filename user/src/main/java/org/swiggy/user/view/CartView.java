package org.swiggy.user.view;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.user.controller.CartController;
import org.swiggy.user.model.Cart;
import org.swiggy.restaurant.model.Restaurant;
import org.swiggy.user.model.User;
import org.swiggy.common.view.CommonView;
import org.swiggy.common.view.CommonViewImpl;

/**
 * <p>
 * Displays and updates the cart of the user
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class CartView {

    private static CartView cartView;
    private final CommonView commonView;
    private final Logger logger;
    private final RestaurantDisplayView restaurantDisplayView;
    private final CartController cartController;

    private CartView() {
        logger = LogManager.getLogger(CartView.class);
        commonView = CommonViewImpl.getInstance();
        restaurantDisplayView = RestaurantDisplayView.getInstance();
        cartController = CartController.getInstance();
    }

    /**
     * <p>
     * Gets the object of the cart view class.
     * </p>
     *
     * @return The cart view object
     */
    public static CartView getInstance() {
        if (null == cartView) {
            cartView = new CartView();
        }

        return cartView;
    }

    /**
     * <p>
     * Adds the selected food to the user cart.
     * </p>
     *
     * @param cart Represents the cart of the user
     * @return True if the food is added to the user cart, false otherwise
     */
    public boolean addFoodToCart(final Cart cart) {
        return cartController.addFoodToCart(cart);
    }

    /**
     * <p>
     * Gets all the items in the user cart.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @return The cart list having list of cart items
     */
    public List<Cart> getCartList(final long userId) {
        return cartController.getCart(userId);
    }

    /**
     * <p>
     * Displays all the items in the user cart.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant}
     */
    public void displayCart(final long userId, final long restaurantId) {
        final List<Cart> cart = getCartList(userId);
        float totalAmount = 0;

        logger.info("""
                Items In Your Cart
                ID | Food Name | Quantity | Rate | Restaurant Name""");

        for(final Cart cartItem : cart) {
            logger.info(String.format("%d %s %d %.2f %s", cart.indexOf(cartItem) + 1, cartItem.getFoodName(),
                    cartItem.getQuantity(), cartItem.getAmount(), cartItem.getRestaurantName()));
            totalAmount += cartItem.getAmount();
        }
        logger.info(String.format("Total Amount: RS %.2f \n", totalAmount));
        displayCartMenu(userId, restaurantId, cart);
    }

    /**
     * <p>
     * Handles the users choice to place order or remove food from the user cart.
     * </p>
     *
     * @param userId Represents the id of current {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant}
     * @param cart Represents the {@link Cart} of the current user
     */
    public void displayCartMenu(final long userId, final long restaurantId, final List<Cart> cart) {
        logger.info("""
                1.Place Order
                2.Remove Item From Cart
                3.Clear All Item From Cart
                4.Add More Food""");
        final int userChoice = commonView.getValue();

        if (-1 == userChoice) {
            restaurantDisplayView.addFoodOrPlaceOrder(userId, restaurantId);
        }

        switch (userChoice) {
            case 1:
                OrderView.getInstance().placeOrder(userId, restaurantId);
                break;
            case 2:
                removeFood(userId, restaurantId, cart);
                break;
            case 3:
                clearCart(userId);
                break;
            case 4:
                restaurantDisplayView.addFoodOrPlaceOrder(userId, restaurantId);
                break;
            default:
                logger.warn("Enter A Valid Option");
                displayCartMenu(userId, restaurantId, cart);
        }
    }

    /**
     * <p>
     * Gets the users choice to remove the food from the user cart.
     * </p>
     *
     * @param userId Represents the id of current {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant}
     * @param cart Represents the {@link Cart} list of the current user
     */
    private void removeFood(final long userId, final long restaurantId, final List<Cart> cart) {
        logger.info("Enter The Item Number To Remove");
        final int itemNumber = commonView.getValue();

        if (-1 == itemNumber) {
            displayCartMenu(userId, restaurantId, cart);
        }
        final int selectedIndex = itemNumber - 1;

        if (selectedIndex >= 0 && selectedIndex < cart.size()) {
            final Cart cartItem = cart.get(selectedIndex);

            if (cartController.removeFood(cartItem.getId())) {
                    logger.info("The Item Is Removed");
            }
        } else {
            logger.warn("Enter The Valid Item Number");
        }
        displayCart(userId, restaurantId);
    }

    /**
     * <p>
     * Handles the users choice to display restaurant or logout.
     * </p>
     *
     * @param userId Represents the id of current {@link User}
     */
    public void displayRestaurantsOrLogout(final long userId) {
        logger.info("""
                1.Continue Food Ordering
                2.Logout""");
        final int userChoice = commonView.getValue();

        if (-1 == userChoice) {
            restaurantDisplayView.displayRestaurants(userId);
        }

        switch (userChoice) {
            case 1:
                restaurantDisplayView.displayRestaurants(userId);
                break;
            case 2:
                logger.info("Your Account Is Logged Out");
                UserView.getInstance().displayMainMenu();
                break;
            default:
                logger.warn("Invalid Option");
                displayRestaurantsOrLogout(userId);
        }
    }

    /**
     * <p>
     * Removes all the food from the user cart.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    public void clearCart(final long userId) {
        if (cartController.clearCart(userId)) {
            logger.info("Your Cart Is Empty");
        }
        displayRestaurantsOrLogout(userId);
    }
}