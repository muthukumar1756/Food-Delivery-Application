package org.swiggy.service.impl1;

import java.util.Map;

import org.swiggy.model.Food;
import org.swiggy.model.User;
import org.swiggy.service.CartService;

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
     * @param food Represents the current {@link Food} selected by the user
     * @param user Represents the current {@link User}
     * @param quantity Represents the quantity of the {@link Food} given by the current user
     * @return True if the food is added to the user cart, false otherwise
     */
    @Override
    public boolean addFoodToCart(final Food food, final User user, final int quantity, final int restaurantId) {
        final int foodQuantity = food.getFoodQuantity();
        final int currentFoodQuantity = foodQuantity - quantity;

        if (foodQuantity < 0) {
            return false;
        } else {
            food.updateQuantity(currentFoodQuantity);
            user.addFoodToCart(food, quantity);

            return true;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param user Represents the current {@link User}
     * @return The map having all the foods from the user cart
     */
    @Override
    public Map<Food, Integer> getCart(final User user) {
        return user.getCartItems();
    }

    /**
     * {@inheritDoc}
     *
     * @param user Represents the current {@link User}
     * @param food Represents the current {@link Food} selected by the user
     * @return True if the food is removed,false otherwise
     */
    @Override
    public boolean removeFood(final User user, final Food food) {
        final Map<Food, Integer> cart = user.getCartItems();

        cart.remove(food);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param user Represents the current {@link User}
     * @return The true if the cart is cleared, false otherwise
     */
    @Override
    public boolean clearCart(final User user) {
        user.getCartItems().clear();

        return true;
    }
}
