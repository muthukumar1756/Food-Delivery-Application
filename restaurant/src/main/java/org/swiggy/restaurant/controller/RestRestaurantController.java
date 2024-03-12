package org.swiggy.restaurant.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import java.util.List;

import org.swiggy.restaurant.exception.FoodDataLoadFailureException;
import org.swiggy.restaurant.exception.MenuCardNotFoundException;
import org.swiggy.restaurant.exception.RestaurantDataLoadFailureException;
import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.FoodType;
import org.swiggy.restaurant.model.Restaurant;
import org.swiggy.restaurant.model.RestaurantDataUpdateType;
import org.swiggy.restaurant.service.RestaurantService;
import org.swiggy.restaurant.service.impl2.RestaurantServiceImpl;

@Path("/restaurant")
public class RestRestaurantController {

    private final RestaurantService restaurantService;
    private static RestRestaurantController restRestaurantController;

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
        if (null == restRestaurantController) {
            restRestaurantController = new RestRestaurantController();
        }

        return restRestaurantController;
    }

    /**
     * <p>
     * Gets the user if the id matches.
     * </p>
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     * @return Response object of the http request
     */
    @Path("/{restaurantId}")
    @GET
    @Produces("application/json")
    public Response getRestaurantById(@PathParam("restaurantId") final long restaurantId) {
        final Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        if (null != restaurant) {
            final ObjectMapper objectMapper = new ObjectMapper();
            final JsonNode jsonNode = objectMapper.valueToTree(restaurant);

            return Response.ok(jsonNode).build();
        }

        return Response.noContent().entity("Enter A Valid Restaurant Id").build();
    }

    /**
     * <p>
     * Creates the new restaurant user profile.
     * </p>
     *
     * @param restaurantJson represents the restaurant data
     * @return Response object of the http request
     */
    @Path("/")
    @POST
    @Consumes("application/json")
    public Response createRestaurantProfile(final String restaurantJson) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode node;

        try {
            node = objectMapper.readTree(restaurantJson);
        } catch (JsonProcessingException exception) {
            throw new RestaurantDataLoadFailureException(exception.getMessage());
        }
        final Restaurant restaurant = new Restaurant();

        restaurant.setName(node.get("name").asText());
        restaurant.setEmailId(node.get("emailId").asText());
        restaurant.setPhoneNumber(node.get("phoneNumber").asText());
        restaurant.setPassword(node.get("password").asText());

        if (restaurantService.createRestaurantProfile(restaurant)) {
            return Response.ok("Restaurant Created").build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Enter A Valid Restaurant Data").build();
    }

    /**
     * <p>
     * Gets all the restaurants.
     * </p>
     *
     * @return Response object of the http request
     */
    @Path("/")
    @GET
    @Produces("application/json")
    public Response getAllRestaurants() {
        final List<Restaurant> restaurants = restaurantService.getRestaurants();

        if (null != restaurants) {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonArray;

            try {
                jsonArray = objectMapper.writeValueAsString(restaurants);
            } catch (JsonProcessingException exception) {
                throw new RestaurantDataLoadFailureException(exception.getMessage());
            }

            return Response.ok(jsonArray).build();
        }

        return Response.noContent().entity("The Restaurants Data Not Available").build();
    }

    /**
     * <p>
     * Gets the menucard of the selected restaurant by the user.
     * </p>
     *
     * @param restaurantId Represents the id of the Restaurant
     * @param foodTypeId Represents the id of the food type.
     * @return Response object of the http request
     */
    @Path("/{restaurantId}/{foodTypeId}")
    @GET
    @Produces("application/json")
    public Response getMenuCard(@PathParam("restaurantId") final long restaurantId,
                                  @PathParam("foodTypeId") final int foodTypeId) {
         final List<Food> menuCard = restaurantService.getMenuCard(restaurantId, foodTypeId);

         if (null !=  menuCard) {
             final ObjectMapper objectMapper = new ObjectMapper();
             final String jsonArray;

             try {
                 jsonArray = objectMapper.writeValueAsString(menuCard);
             } catch (JsonProcessingException exception) {
                 throw new MenuCardNotFoundException(exception.getMessage());
             }

             return Response.ok(jsonArray).build();
         }

         return Response.status(Response.Status.NO_CONTENT).entity("MenuCard Not Available").build();
    }

    /**
     * <p>
     * Adds food to the restaurant.
     * </p>
     *
     * @param foodJson Represent the
     * @param restaurantId Represents the id of the Restaurant
     * @return Response object of the http request
     */
    @Path("/{restaurantId}")
    @POST
    @Consumes("application/json")
    public Response addFood(final String foodJson, @PathParam("restaurantId") final long restaurantId) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode node;

        try {
            node = objectMapper.readTree(foodJson);
        } catch (JsonProcessingException exception) {
            throw new FoodDataLoadFailureException(exception.getMessage());
        }
        final Food food = new Food();

        food.setFoodName(node.get("name").asText());
        food.setFoodRate((float) node.get("rate").asDouble());
        food.setFoodQuantity(node.get("foodQuantity").asInt());
        final String foodType = node.get("foodType").asText();

        food.setFoodType(FoodType.valueOf(foodType));
        if (restaurantService.addFood(food, restaurantId)) {
            return Response.ok("Food Data Added").build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Unsuccessful Check the Data").build();
    }

    /**
     * <p>
     * Removes the food from the restaurant.
     * </p>
     *
     * @param foodId Represents the id of the current {@link Food} selected by the user
     * @return Response object of the http request
     */
    @Path("/{foodId}")
    @DELETE
    public Response removeFood(@PathParam("foodId") final long foodId) {
        if (restaurantService.removeFood(foodId)) {
            return Response.status(Response.Status.ACCEPTED).entity("The Food Item Is Removed").build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Enter Valid Food Id").build();
    }

    /**
     * <p>
     * Updates the data of the current restaurant user.
     * </p>
     *
     * @param restaurantId Represents the id 0f the current {@link Restaurant}
     * @param restaurantData Represents the data of the current user to be updated
     * @return Response object of the http request
     */
    @Path("/{restaurantId}")
    @PUT
    @Consumes("application/json")
    public Response updateRestaurantData(@PathParam("restaurantId") final long restaurantId,
                                     final String restaurantData)  {
        RestaurantDataUpdateType type;
        String updateValue;

        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode jsonNode;

        try {
            jsonNode = objectMapper.readTree(restaurantData);
            final String typeString = jsonNode.get("type").asText();

            type = RestaurantDataUpdateType.valueOf(typeString);
            updateValue = jsonNode.get("restaurantData").asText();
        } catch (JsonProcessingException message) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update data format").build();
        }

        if (restaurantService.updateRestaurantData(restaurantId, updateValue, type)) {
            return Response.status(Response.Status.ACCEPTED).entity("The Restaurant Data Updated").build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("The Data Entered Not In Acceptable Format").build();
    }
}