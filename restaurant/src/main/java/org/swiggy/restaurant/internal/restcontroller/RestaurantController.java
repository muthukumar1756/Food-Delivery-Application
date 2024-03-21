package org.swiggy.restaurant.internal.restcontroller;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;

import java.util.List;

import org.swiggy.common.json.JacksonFactory;
import org.swiggy.common.json.JsonArray;
import org.swiggy.restaurant.internal.service.RestaurantService;
import org.swiggy.restaurant.internal.service.impl.RestaurantServiceImpl;
import org.swiggy.common.json.JsonObject;
import org.swiggy.restaurant.model.Restaurant;
import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.RestaurantData;
import org.swiggy.validator.hibernatevalidator.ValidatorFactory;
import org.swiggy.validator.validatorgroup.Restaurant.LoginRestaurantValidation;
import org.swiggy.validator.validatorgroup.Restaurant.PostRestaurantValidator;
import org.swiggy.validator.validatorgroup.Restaurant.GetRestaurantValidator;
import org.swiggy.validator.validatorgroup.Restaurant.PutRestaurantValidator;
import org.swiggy.validator.validatorgroup.food.DeleteFoodValidator;
import org.swiggy.validator.validatorgroup.food.PostFoodValidator;
import org.swiggy.validator.validatorgroup.food.GetFoodValidator;

/**
 * <p>
 * Handles the restaurant related operation and responsible for receiving input through rest api and processing it.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
@Path("/restaurant")
public class RestaurantController {

    private static RestaurantController restaurantController;
    private final RestaurantService restaurantService;
    private final JacksonFactory jacksonFactory;
    private final ValidatorFactory validatorFactory;

    private RestaurantController() {
        restaurantService = RestaurantServiceImpl.getInstance();
        jacksonFactory = JacksonFactory.getInstance();
        validatorFactory = ValidatorFactory.getInstance();
    }

    /**
     * <p>
     * Gets the restaurant controller object.
     * </p>
     *
     * @return The restaurant controller object
     */
    public static RestaurantController getInstance() {
        if (null == restaurantController) {
            restaurantController = new RestaurantController();
        }

        return restaurantController;
    }

    /**
     * <p>
     * Creates the new restaurant profile.
     * </p>
     *
     * @param restaurant represents the data of the restaurant
     * @return byte array of json object
     */
    @POST
    @Consumes("application/json")
    public byte[] createRestaurantProfile(final Restaurant restaurant) {
        final JsonArray jsonViolations = validatorFactory.getViolations(restaurant, PostRestaurantValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jacksonFactory.createObjectNode();

        if (restaurantService.createRestaurantProfile(restaurant)) {
            return jsonObject.put("status", "successful restaurant profile was created").asBytes();
        }

        return jsonObject.put("status", "unsuccessful restaurant profile creation failed").asBytes();
    }

    /**
     * <p>
     * Gets the restaurant if the id matches.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     * @return byte array of json object
     */
    @Path("/{restaurantId}")
    @GET
    @Produces("application/json")
    public byte[] getRestaurantById(@PathParam("restaurantId") final long restaurantId) {
        final Restaurant restaurantPojo = new Restaurant();

        restaurantPojo.setId(restaurantId);
        final JsonArray jsonViolations = validatorFactory.getViolations(restaurantPojo, GetRestaurantValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jacksonFactory.createObjectNode();
        final Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        if (null == restaurant) {
            return jsonObject.put("status", "enter a valid restaurant id").asBytes();
        }

        return jsonObject.build(restaurant).asBytes();
    }

    /**
     * <p>
     * Gets the restaurant profile if the phone_number and password matches.
     * </p>
     *
     * @param restaurantData Represents the data of the restaurant
     * @return byte array of json object
     */
    @Path("/login")
    @POST
    public byte[] restaurantLogin(final String restaurantData) {
        final JsonArray jsonViolations = jacksonFactory.createArrayNode();
        final JsonObject jsonObject = jacksonFactory.createObjectNode().toJsonNode(restaurantData);
        final String loginType = jsonObject.get("type");
        RestaurantData type = null;

        try {
            type = RestaurantData.valueOf(loginType);
        } catch (IllegalArgumentException exception) {
            jsonViolations.add(jacksonFactory.createObjectNode().put("error", "enter valid type of data to login"));
        }
        final String value = jsonObject.get("restaurantdata");
        final String password = jsonObject.get("password");
        final Restaurant restaurant = new Restaurant();

        restaurant.setPassword(password);
        if (null != type) {
            switch (type) {
                case PHONE_NUMBER -> restaurant.setPhoneNumber(value);
                case EMAIL_ID -> restaurant.setEmailId(value);
            }
        }
        jsonViolations.addArray(validatorFactory.getViolations(restaurant, LoginRestaurantValidation.class));

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final Restaurant restaurantPojo = restaurantService.getRestaurant(type, value, password);

        if (null != restaurantPojo) {
            final String message = String.join("","restaurant login successful welcome ",
                    restaurantPojo.getName());

            return jacksonFactory.createObjectNode().put("status", message).asBytes();
        }

        return jacksonFactory.createObjectNode().put("status", "restaurant login failed").asBytes();
    }

    /**
     * <p>
     * Gets all the restaurants.
     * </p>
     *
     * @return byte array of json object
     */
    @GET
    @Produces("application/json")
    public byte[] getAllRestaurants() {
        final List<Restaurant> restaurants = restaurantService.getRestaurants();
        final JsonArray jsonArray = jacksonFactory.createArrayNode();

        if (null == restaurants) {
            return jacksonFactory.createObjectNode().put("status", "no available restaurants").asBytes();
        }

        return jsonArray.build(restaurants).asBytes();
    }

    /**
     * <p>
     * Gets the menucard from the restaurant
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     * @param menuCardTypeId Represents the id of the food type.
     * @return byte array of json object
     */
    @Path("/{restaurantId}/{foodTypeId}")
    @GET
    @Produces("application/json")
    public byte[] getMenuCard(@PathParam("restaurantId") final long restaurantId,
                                  @PathParam("foodTypeId") final int menuCardTypeId) {
        final Restaurant restaurant = new Restaurant();

        restaurant.setId(restaurantId);
        final JsonArray jsonViolations = validatorFactory.getViolations(restaurant, GetFoodValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final List<Food> menuCard = restaurantService.getMenuCard(restaurantId, menuCardTypeId);

        if (null == menuCard) {
            return jacksonFactory.createObjectNode().put("status", "no available foods or enter valid restaurant id")
                    .asBytes();
        }

        return jacksonFactory.createArrayNode().build(menuCard).asBytes();
    }

    /**
     * <p>
     * Adds food to the restaurant.
     * </p>
     *
     * @param food Represents the food
     * @param restaurantId Represents the id of the restaurant
     * @return byte array of json object
     */
    @Path("/{restaurantId}")
    @POST
    @Consumes("application/json")
    public byte[] addFood(final Food food, @PathParam("restaurantId") final long restaurantId) {
        final Restaurant restaurant = new Restaurant();

        restaurant.setId(restaurantId);
        final JsonArray jsonViolations = validatorFactory.getViolations(restaurant, GetRestaurantValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        jsonViolations.addArray(validatorFactory.getViolations(food, PostFoodValidator.class));

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jacksonFactory.createObjectNode();

        if (restaurantService.addFood(food, restaurantId)) {
            return jsonObject.put("status", "successful food was added").asBytes();
        }

        return jsonObject.put("status", "unsuccessful adding food was failed").asBytes();
    }

    /**
     * <p>
     * Removes the food from the restaurant.
     * </p>
     *
     * @param foodId Represents the id of the food
     * @return byte array of json object
     */
    @Path("/{foodId}")
    @DELETE
    public byte[] removeFood(@PathParam("foodId") final long foodId) {
        final Food food = new Food();

        food.setId(foodId);
        final JsonArray jsonViolations = validatorFactory.getViolations(food, DeleteFoodValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jacksonFactory.createObjectNode();

        if (restaurantService.removeFood(foodId)) {
            return jsonObject.put("status", "successful food was removed").asBytes();
        }

        return jsonObject.put("status", "unsuccessful removing food was failed").asBytes();
    }

    /**
     * <p>
     * Updates the data of the restaurant.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     * @param restaurantData Represents the data of the restaurant to be updated
     * @return byte array of json object
     */
    @Path("/{restaurantId}")
    @PUT
    @Consumes("application/json")
    public byte[] updateRestaurantData(@PathParam("restaurantId") final long restaurantId,
                                     final String restaurantData)  {
        final JsonArray jsonViolations = jacksonFactory.createArrayNode();
        final JsonObject jsonObject = jacksonFactory.createObjectNode().toJsonNode(restaurantData);
        final String typeString = jsonObject.get("type");
        RestaurantData type = null;

        try {
            type = RestaurantData.valueOf(typeString);
        } catch (IllegalArgumentException exception) {
            jsonViolations.add(jacksonFactory.createObjectNode()
                    .put("error", "enter valid type of data to be updated"));
        }
        final String updateValue = jsonObject.get("restaurantdata");
        final Restaurant restaurant = new Restaurant();

        restaurant.setId(restaurantId);

        if (null != type) {
            switch (type) {
                case NAME -> restaurant.setName(updateValue);
                case PASSWORD -> restaurant.setPassword(updateValue);
                case EMAIL_ID -> restaurant.setEmailId(updateValue);
                case PHONE_NUMBER -> restaurant.setPhoneNumber(updateValue);
            }
        }
        jsonViolations.addArray(validatorFactory.getViolations(restaurant, PutRestaurantValidator.class));

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }

        if (restaurantService.updateRestaurantData(restaurantId, updateValue, type)) {
            return jacksonFactory.createObjectNode().put("status", "successful restaurant profile updated").asBytes();
        }

        return jacksonFactory.createObjectNode().put("status", "unsuccessful restaurant profile updation failed").
                asBytes();
    }
}