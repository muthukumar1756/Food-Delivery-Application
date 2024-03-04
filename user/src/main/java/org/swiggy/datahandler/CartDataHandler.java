package org.swiggy.datahandler;

import org.swiggy.model.Cart;
import org.swiggy.model.User;

import java.util.List;

/**
 * <p>
 * Provides data base service for the user cart.
 * </p>
 *
 * @author Muthu kumar v
 * @version 1.1
 */
public interface CartDataHandler {

    /**
     * <p>
     * Adds the selected food to the user cart.
     * </p>
     *
     * @param cart Represents the cart of the user
     * @return True if the food is added to the user cart, false otherwise
     */
    boolean addFoodToCart(final Cart cart);

    /**
     * <p>
     * Gets the cart of the current user.
     * </p>
     *
     * @param userId Represents the id 0f the current {@link User}
     * @return The map having all the foods from the user cart
     */
    List<Cart> getCart(final long userId);

    /**
     * <p>
     * Removes the selected food from the user cart.
     * </p>
     *
     * @param cartId Represents the id 0f the user cart
     * @return True if the food is removed,false otherwise
     */
    boolean removeFood(final long cartId);

    /**
     * <p>
     * Remove all the foods from the user cart.
     * </p>
     *
     * @param userId Represents the id 0f the current {@link User}
     * @return The true if the cart is cleared, false otherwise
     */
    boolean clearCart(final long userId);
}
