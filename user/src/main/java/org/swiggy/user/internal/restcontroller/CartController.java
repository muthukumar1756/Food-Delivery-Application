package org.swiggy.user.internal.restcontroller;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;

import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.swiggy.common.json.JacksonFactory;
import org.swiggy.common.json.JsonArray;
import org.swiggy.common.json.JsonObject;
import org.swiggy.user.model.Cart;
import org.swiggy.user.model.User;
import org.swiggy.user.internal.service.CartService;
import org.swiggy.user.internal.service.impl.CartServiceImpl;
import org.swiggy.validator.validatorgroup.cart.ClearCartValidator;
import org.swiggy.validator.validatorgroup.cart.DeleteCartValidator;
import org.swiggy.validator.validatorgroup.cart.PostCartValidator;
import org.swiggy.validator.validatorgroup.cart.GetCartValidator;

/**
 * <p>
 * Handles the users cart related operation and responsible for receiving user input through rest api and processing it.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
@Path("/cart")
public class CartController {

    private static CartController cartController;
    private final CartService cartService;
    private final JacksonFactory jacksonFactory;
    private final Validator validator;

    private CartController() {
        cartService = CartServiceImpl.getInstance();
        jacksonFactory = JacksonFactory.getInstance();
        validator = Validation.byProvider(HibernateValidator.class).configure()
                .messageInterpolator(new ParameterMessageInterpolator()).buildValidatorFactory().getValidator();
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
     * @return byte array of json object
     */
    @POST
    @Consumes("application/json")
    public byte[] addFoodToCart(final Cart cart) {
        final Set<ConstraintViolation<Cart>> violationSet = validator.validate(cart,
                PostCartValidator.class);

        if (!violationSet.isEmpty()) {
            final JsonArray jsonViolations = jacksonFactory.createArrayNode();

            for (final ConstraintViolation violation : violationSet) {
                jsonViolations.add(jacksonFactory.createObjectNode().put(violation.getPropertyPath().toString(), violation.getMessage()));
            }

            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jacksonFactory.createObjectNode();

        if (cartService.addFoodToCart(cart)) {
            return jsonObject.put("status", "successful cart item added").asBytes();
        }

        return jsonObject.put("status", "unsuccessful adding cart item failed").asBytes();
    }

    /**
     * <p>
     * Gets the cart of the user.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return byte array of json object
     */
    @Path("/{userId}")
    @GET
    @Produces("application/json")
    public byte[] getCart(@PathParam("userId") final long userId) {
        final Cart cart = new Cart();

        cart.setUserId(userId);
        final Set<ConstraintViolation<Cart>> violationSet = validator.validate(cart,
                GetCartValidator.class);

        if (!violationSet.isEmpty()) {
            final JsonArray jsonViolations = jacksonFactory.createArrayNode();

            for (final ConstraintViolation violation : violationSet) {
                jsonViolations.add(jacksonFactory.createObjectNode().put(violation.getPropertyPath().toString(), violation.getMessage()));
            }

            return jsonViolations.asBytes();
        }
        final List<Cart> cartList = cartService.getCart(userId);

        if (null == cartList) {
            return jacksonFactory.createObjectNode().put("status", "your cart is empty or user id is invalid").asBytes();
        }

        return jacksonFactory.createArrayNode().build(cartList).asBytes();
    }

    /**
     * <p>
     * Removes the food selected by the user.
     * </p>
     *
     * @param cartId Represents the id of the user cart
     * @return byte array of json object
     */
    @Path("/{cartId}")
    @DELETE
    public byte[] removeFood(@PathParam("cartId") final long cartId) {
        final Cart cart = new Cart();

        cart.setId(cartId);
        final Set<ConstraintViolation<Cart>> violationSet = validator.validate(cart,
                DeleteCartValidator.class);

        if (!violationSet.isEmpty()) {
            final JsonArray jsonViolations = jacksonFactory.createArrayNode();

            for (final ConstraintViolation violation : violationSet) {
                jsonViolations.add(jacksonFactory.createObjectNode().put(violation.getPropertyPath().toString(), violation.getMessage()));
            }

            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jacksonFactory.createObjectNode();

        if (cartService.removeFood(cartId)) {
            return jsonObject.put("status", "successful food was removed").asBytes();
        }

        return jsonObject.put("status", "unsuccessful removing food was failed").asBytes();
    }

    /**
     * <p>
     * Remove all the foods from the user cart.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return byte array of json object
     */
    @Path("clear/{userId}")
    @DELETE
    public byte[] clearCart(@PathParam("userId") final long userId) {
        final Cart cart = new Cart();

        cart.setUserId(userId);
        final Set<ConstraintViolation<Cart>> violationSet = validator.validate(cart,
                ClearCartValidator.class);

        if (!violationSet.isEmpty()) {
            final JsonArray jsonViolations = jacksonFactory.createArrayNode();

            for (final ConstraintViolation violation : violationSet) {
                jsonViolations.add(jacksonFactory.createObjectNode().put(violation.getPropertyPath().toString(), violation.getMessage()));
            }

            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jacksonFactory.createObjectNode();

        if (cartService.clearCart(userId)) {
            return jsonObject.put("status", "successful cart was cleared").asBytes();
        }

        return jsonObject.put("status", "unsuccessful clearing cart was failed").asBytes();
    }
}