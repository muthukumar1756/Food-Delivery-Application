package org.swiggy.controller;

import org.swiggy.model.Restaurant;
import org.swiggy.service.RestaurantService;
import org.swiggy.service.impl2.RestaurantServiceImpl;

import javax.ws.rs.*;

@Path("/restaurant")
@Consumes("application/json")
@Produces("application/json")
public class RestRestaurantController {

    private final RestaurantService restaurantService;

    private RestRestaurantController() {
        restaurantService = RestaurantServiceImpl.getInstance();
    }

    /**
     * <p>
     * Gets the restaurant controller object.
     * </p>
     *
     * @return The restaurant controller object
     */
    public static RestRestaurantController getInstance() {
        return SingletonHelper.REST_RESTAURANT_CONTROLLER;
    }

    private static class SingletonHelper {
        public static final RestRestaurantController REST_RESTAURANT_CONTROLLER = new RestRestaurantController();
    }

    /**
     * <p>
     * Gets the user if the id matches.
     * </p>
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     * @return The current restaurant user
     */
    @Path("/{id}")
    @GET
    public Restaurant getRestaurantById(@PathParam("id") final long restaurantId) {
        return restaurantService.getRestaurantById(restaurantId);
    }
}
