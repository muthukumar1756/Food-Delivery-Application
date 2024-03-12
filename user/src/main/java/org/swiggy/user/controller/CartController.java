package org.swiggy.user.controller;

import org.swiggy.user.model.User;
import org.swiggy.user.model.Cart;
import org.swiggy.user.service.CartService;
import org.swiggy.user.service.impl2.CartServiceImpl;

import java.util.List;

/**
 * <p>
 * Handles the users cart related operation and responsible for receiving user input and processing it.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class CartController {

    private static CartController cartController;
    private final CartService cartService;

    private CartController() {
        cartService = CartServiceImpl.getInstance();
    }

    /**
     * <p>
     * Gets the object of the cart controller class.
     * </p>
     *
     * @return The cart controller object
     */
    public static CartController getInstance() {
        if (null == cartController) {
            cartController = new CartController();
        }

        return cartController;
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
        return cartService.addFoodToCart(cart);
    }

    /**
     * <p>
     * Gets the cart of the current user.
     * </p>
     *
     * @param userId Represents the id 0f the current {@link User}
     * @return The list of all foods from the user cart
     */
    public List<Cart> getCart(final long userId) {
        return cartService.getCart(userId);
    }

    /**
     * <p>
     * Removes the food selected by the user.
     * </p>
     *
     * @param cartId Represents the id 0f the user cart
     * @return True if the food is removed,false otherwise
     */
    public boolean removeFood(final long cartId) {
        return cartService.removeFood(cartId);
    }

    /**
     * <p>
     * Remove all the foods from the user cart.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @return The true if the cart is cleared, false otherwise
     */
    public boolean clearCart(final long userId) {
        return cartService.clearCart(userId);
    }
}
