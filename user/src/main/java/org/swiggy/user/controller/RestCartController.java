package org.swiggy.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.swiggy.user.exception.CartDataNotFoundException;
import org.swiggy.user.exception.CartUpdateFailureException;
import org.swiggy.user.model.Cart;
import org.swiggy.user.model.User;
import org.swiggy.user.service.CartService;
import org.swiggy.user.service.impl2.CartServiceImpl;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/cart")
public class RestCartController {

    private static RestCartController restCartController;
    private final CartService cartService;

    private RestCartController() {
        cartService = CartServiceImpl.getInstance();
    }

    /**
     * <p>
     * Gets the object of the cart controller class.
     * </p>
     *
     * @return The cart controller object
     */
    public static RestCartController getInstance() {
        if (null == restCartController) {
            restCartController = new RestCartController();
        }

        return restCartController;
    }

    /**
     * <p>
     * Adds the selected food to the user cart.
     * </p>
     *
     * @param jsonCart Represents the cart of the user
     * @return Response object of the http request
     */
    @Path("/")
    @POST
    @Consumes("application/json")
    public Response addFoodToCart(final String jsonCart) {
        final ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;

        try {
            jsonNode = objectMapper.readTree(jsonCart);
        } catch (JsonProcessingException exception) {
            throw new CartUpdateFailureException(exception.getMessage());
        }
        final Cart cart = new Cart();

        cart.setUserId(jsonNode.get("userId").asLong());
        cart.setRestaurantId(jsonNode.get("restaurantId").asLong());
        cart.setFoodId(jsonNode.get("foodId").asLong());
        cart.setQuantity(jsonNode.get("quantity").asInt());
        cart.setAmount((float) jsonNode.get("amount").asDouble());

        if (cartService.addFoodToCart(cart)) {
            return Response.ok("Cart Object Added").build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Enter A Valid Cart Item").build();
    }

    /**
     * <p>
     * Gets the cart of the current user.
     * </p>
     *
     * @param userId Represents the id 0f the current {@link User}
     * @return Response object of the http request
     */
    @Path("/{userId}")
    @GET
    @Produces("application/json")
    public Response getCart(@PathParam("userId") final long userId) {
        final List<Cart> cartList = cartService.getCart(userId);

        if (null != cartList) {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonArray;

            try {
                jsonArray = objectMapper.writeValueAsString(cartList);
            } catch (JsonProcessingException exception) {
                throw new CartDataNotFoundException(exception.getMessage());
            }

            return Response.ok(jsonArray).build();
        }

        return Response.status(Response.Status.NO_CONTENT).entity("Enter A Valid User Id").build();
    }

    /**
     * <p>
     * Removes the food selected by the user.
     * </p>
     *
     * @param cartId Represents the id 0f the user cart
     * @return Response object of the http request
     */
    @Path("/{cartId}")
    @DELETE
    public Response removeFood(@PathParam("cartId") final long cartId) {
        if (cartService.removeFood(cartId)) {
            return Response.status(Response.Status.ACCEPTED).entity("The Food Item Is Removed").build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Enter Valid Food Id").build();
    }

    /**
     * <p>
     * Remove all the foods from the user cart.
     * </p>
     *
     * @param userId Represents the id 0f the current {@link User}
     * @return Response object of the http request
     */
    @Path("/{userId}")
    @DELETE
    public Response clearCart(@PathParam("userId") final long userId) {
        if (cartService.clearCart(userId)) {
            return Response.status(Response.Status.ACCEPTED).entity("The User Cart Is Cleared").build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Enter Valid User Id").build();
    }
}
