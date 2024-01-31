package org.swiggy.controller;

import org.swiggy.model.User;
import org.swiggy.model.Food;
import org.swiggy.service.CartService;
import org.swiggy.service.impl2.CartServiceImpl;

import java.util.Map;

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
     * @param food Represents the current {@link Food} selected by the user
     * @param user Represents the current {@link User}
     * @param quantity Represents the quantity of the {@link Food} given by the current user
     * @param restaurantId Represents the id of the current restaurant
     * @return True if the food is added to the user cart, false otherwise
     */
    public boolean addFoodToCart(final Food food, final User user, final int quantity, final int restaurantId) {
        return cartService.addFoodToCart(food, user, quantity, restaurantId);
    }

    /**
     * <p>
     * Gets the cart of the current user.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @return The map having all the foods from the user cart
     */
    public Map<Food, Integer> getCart(final User user) {
        return cartService.getCart(user);
    }

    /**
     * <p>
     * Removes the food selected by the user.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @param food  Represents the current {@link Food} selected by the user
     */
    public boolean removeFood(final User user, final Food food) {
        return cartService.removeFood(user, food);
    }

    /**
     * <p>
     * Remove all the foods from the user cart.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @return The true if the cart is cleared, false otherwise
     */
    public boolean clearCart(final User user) {
        return cartService.clearCart(user);
    }
}
