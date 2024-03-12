package org.swiggy.user.service.impl1;

import java.util.List;

import org.swiggy.user.model.Cart;
import org.swiggy.restaurant.model.Food;
import org.swiggy.user.model.User;
import org.swiggy.user.service.CartService;

/**
 * <p>
 * Implements the service of the user cart related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class CartServiceImpl implements CartService {

    private static CartService cartService;

    private CartServiceImpl() {
    }

    /**
     * <p>
     * Gets the cart service implementation class object.
     * </p>
     *
     * @return The cart service implementation object
     */
    public static CartService getInstance() {
        if (null == cartService) {
            cartService = new CartServiceImpl();
        }

        return cartService;
    }

    /**
     * {@inheritDoc}
     *
     * @param cart Represents the cart of the user
     * @return True if the food is added to the user cart, false otherwise
     */
    @Override
    public boolean addFoodToCart(final Cart cart) {
        final int foodQuantity = cart.getQuantity();
        final int currentFoodQuantity = foodQuantity - cart.getQuantity();

        if (0 < foodQuantity) {
            return false;
        } else {

            return true;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id 0f the current {@link User}
     * @return The map having all the foods from the user cart
     */
    @Override
    public List<Cart> getCart(final long userId) {
        return null;
    }

    @Override
    public boolean removeFood(long cartId) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id 0f the current {@link User}
     * @param food Represents the current {@link Food} selected by the user
     * @return True if the food is removed,false otherwise
     */
    public boolean removeFood(final long userId, final Food food) {


        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id 0f the current {@link User}
     * @return The true if the cart is cleared, false otherwise
     */
    @Override
    public boolean clearCart(final long userId) {

        return true;
    }
}
