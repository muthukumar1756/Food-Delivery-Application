package org.swiggy.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.swiggy.user.exception.UserDataLoadFailureException;
import org.swiggy.user.model.User;
import org.swiggy.user.service.UserService;
import org.swiggy.user.service.impl2.UserServiceImpl;
import org.swiggy.user.view.UserDataUpdateType;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/user")
public class RestUserController {
    private static RestUserController restUserController;
    private final UserService userService;

    private RestUserController() {
        userService = UserServiceImpl.getInstance();
    }

    /**
     * <p>
     * Gets the object of the user controller class.
     * </p>
     *
     * @return The user controller object
     */
    public static RestUserController getInstance() {
        if (null == restUserController) {
            restUserController = new RestUserController();
        }

        return restUserController;
    }

    /**
     * <p>
     * Creates the new user.
     * </p>
     *
     * @param userJson Represents the {@link User} data
     * @return Response object of the http request
     */
    @Path("/")
    @POST
    @Consumes("application/json")
    public Response createUserProfile(final String userJson) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode jsonNode;

        try {
            jsonNode = objectMapper.readTree(userJson);
        } catch (JsonProcessingException exception) {
            throw new UserDataLoadFailureException(exception.getMessage());
        }
        final User user = new User();

        user.setName(jsonNode.get("name").asText());
        user.setEmailId(jsonNode.get("emailId").asText());
        user.setPhoneNumber(jsonNode.get("phoneNumber").asText());
        user.setPassword(jsonNode.get("password").asText());

        if (userService.createUserProfile(user)) {
            return Response.ok("User Created").build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Enter Valid User Details").build();
    }

    /**
     * <p>
     * Gets the user if the id matches.
     * </p>
     *
     * @param userId Represents the password of the current user
     * @return Response object of the http request
     */
    @Path("/{userId}")
    @GET
    @Produces("application/json")
    public Response getUserById(@PathParam("userId") final long userId) {
        final User user = userService.getUserById(userId);

        if (null != user) {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            final JsonNode jsonNode = objectMapper.valueToTree(user);

            return Response.ok(jsonNode).build();
        }

        return Response.noContent().entity("Enter A Valid User Id").build();
    }

    /**
     * <p>
     * Updates the data of the current user.
     * </p>
     *
     * @param userId Represents the id 0f the current {@link User}
     * @param userData Represents the data of the current user to be updated
     * @return Response object of the http request
     */
    @Path("/{userId}")
    @PUT
    @Consumes("application/json")
    public Response updateUserData(@PathParam("userId") final long userId, final String userData) {
        final ObjectMapper objectMapper = new ObjectMapper();
        UserDataUpdateType type;
        String updateValue;

        try {
            final JsonNode jsonNode = objectMapper.readTree(userData);
            final String typeString = jsonNode.get("type").asText();

            type = UserDataUpdateType.valueOf(typeString);
            updateValue = jsonNode.get("userData").asText();
        } catch (JsonProcessingException message) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update data format").build();
        }

        if (userService.updateUserData(userId, type, updateValue)) {
            return Response.status(Response.Status.ACCEPTED).entity("The User Data Updated").build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("The Data Entered Not In Acceptable Format").build();
    }
}
