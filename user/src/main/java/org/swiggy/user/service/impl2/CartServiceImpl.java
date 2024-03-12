package org.swiggy.user.service.impl2;

import org.swiggy.user.datahandler.CartDataHandler;
import org.swiggy.user.datahandler.impl.CartDataHandlerImpl;
import org.swiggy.user.model.Cart;
import org.swiggy.user.model.User;
import org.swiggy.user.service.CartService;

import java.util.List;

/**
 * <p>
 * Implements the service of the user cart related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class CartServiceImpl implements CartService{

    private static CartService cartService;
    private final CartDataHandler cartDataHandler;

    private CartServiceImpl() {
        cartDataHandler = CartDataHandlerImpl.getInstance();
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
        return cartDataHandler.addFoodToCart(cart);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id 0f the current {@link User}
     * @return The list of all foods from the user cart
     */
    @Override
    public List<Cart> getCart(final long userId) {
        return cartDataHandler.getCart(userId);
    }

    /**
     * {@inheritDoc}
     *
     * @param cartId Represents the id of the user cart
     * @return True if the food is removed,false otherwise
     */
    @Override
    public boolean removeFood(final long cartId) {
        return cartDataHandler.removeFood(cartId);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the current {@link User}
     * @return The true if the cart is cleared, false otherwise
     */
    @Override
    public boolean clearCart(final long userId) {
        return cartDataHandler.clearCart(userId);
    }
}
