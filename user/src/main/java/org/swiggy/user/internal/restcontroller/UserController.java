package org.swiggy.user.internal.restcontroller;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;

import org.swiggy.common.json.JacksonFactory;
import org.swiggy.common.json.JsonArray;
import org.swiggy.common.json.JsonObject;
import org.swiggy.user.internal.service.UserService;
import org.swiggy.user.internal.service.impl.UserServiceImpl;
import org.swiggy.user.model.User;
import org.swiggy.user.model.UserData;
import org.swiggy.validator.hibernatevalidator.ValidatorFactory;
import org.swiggy.validator.validatorgroup.user.LoginUserValidator;
import org.swiggy.validator.validatorgroup.user.PostUserValidator;
import org.swiggy.validator.validatorgroup.user.GetUserValidator;
import org.swiggy.validator.validatorgroup.user.PutUserValidator;

/**
 * <p>
 * Handles the user related operation and responsible for processing user input through rest api
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
@Path("/user")
public class UserController {
    private static UserController userController;
    private final UserService userService;
    private final JacksonFactory jacksonFactory;
    private final ValidatorFactory validatorFactory;

    private UserController() {
        userService = UserServiceImpl.getInstance();
        jacksonFactory = JacksonFactory.getInstance();
        validatorFactory = ValidatorFactory.getInstance();
    }

    /**
     * <p>
     * Gets the object of the user controller class.
     * </p>
     *
     * @return The user controller object
     */
    public static UserController getInstance() {
        if (null == userController) {
            userController = new UserController();
        }

        return userController;
    }

    /**
     * <p>
     * Creates the new user.
     * </p>
     *
     * @param user Represents the {@link User}
     * @return byte array of json object
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public byte[] createUserProfile(final User user) {
        final JsonArray jsonViolations = validatorFactory.getViolations(user, PostUserValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jacksonFactory.createObjectNode();

        if (userService.createUserProfile(user)) {
            return jsonObject.put("status", "successful user profile was created").asBytes();
        }

        return jsonObject.put("status", "unsuccessful user profile creation failed").asBytes();
    }

    /**
     * <p>
     * Gets the user if the phone_number and password matches.
     * </p>
     *
     * @param userData Represents the data of the user
     * @return byte array of json object
     */
    @Path("/login")
    @POST
    public byte[] userLogin(final String userData) {
        final JsonArray jsonViolations = jacksonFactory.createArrayNode();
        final JsonObject jsonObject = jacksonFactory.createObjectNode().toJsonNode(userData);
        final String loginType = jsonObject.get("type");
        UserData type = null;

        try {
            type = UserData.valueOf(loginType);
        } catch (IllegalArgumentException exception) {
            jsonViolations.add(jacksonFactory.createObjectNode()
                    .put("error", "enter valid type of data to login"));
        }
        final String value = jsonObject.get("userdata");
        final String password = jsonObject.get("password");
        final User user = new User();

        user.setPassword(password);
        if (null != type) {
            switch (type) {
                case PHONE_NUMBER -> user.setPhoneNumber(value);
                case EMAIL_ID -> user.setEmailId(value);
            }
        }
        jsonViolations.addArray(validatorFactory.getViolations(user, LoginUserValidator.class));

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final User userPojo = userService.getUser(type, value, password);

        if (null != userPojo) {
            final String message = String.join("","user login successful welcome ",userPojo.getName());

            return jacksonFactory.createObjectNode().put("status", message).asBytes();
        }

        return jacksonFactory.createObjectNode().put("status", "user login failed").asBytes();
    }

    /**
     * <p>
     * Gets the user if the id matches.
     * </p>
     *
     * @param userId Represents the password of the current user
     * @return byte array of json object
     */
    @Path("/{userId}")
    @GET
    @Produces("application/json")
    public byte[] getUserById(@PathParam("userId") final long userId) {
        final User userPojo = new User();

        userPojo.setId(userId);
        final JsonArray jsonViolations = validatorFactory.getViolations(userPojo, GetUserValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jacksonFactory.createObjectNode();
        final User user = userService.getUserById(userId);

        if (null == user) {
            return jsonObject.put("status", "Enter A Valid User Id").asBytes();
        }

        return jsonObject.build(user).asBytes();
    }

    /**
     * <p>
     * Updates the data of the current user.
     * </p>
     *
     * @param userId Represents the id 0f the {@link User}
     * @param userData Represents the data of the user to be updated
     * @return byte array of json object
     */
    @Path("/{userId}")
    @PUT
    @Consumes("application/json")
    public byte[] updateUserData(@PathParam("userId") final long userId, final String userData) {
        final JsonArray jsonViolations = jacksonFactory.createArrayNode();
        final JsonObject jsonObject = jacksonFactory.createObjectNode().toJsonNode(userData);
        final String typeString = jsonObject.get("type");
        UserData type = null;

        try {
            type = UserData.valueOf(typeString);
        } catch (IllegalArgumentException exception) {
            jsonViolations.add(jacksonFactory.createObjectNode()
                    .put("error", "enter valid type of data to be updated"));
        }
        final String updateValue = jsonObject.get("userdata");
        final User user = new User();

        user.setId(userId);

        if (null != type) {
            switch (type) {
                case NAME -> user.setName(userData);
                case PHONE_NUMBER -> user.setPhoneNumber(userData);
                case PASSWORD -> user.setPassword(userData);
                case EMAIL_ID -> user.setEmailId(userData);
            }
        }
        jsonViolations.addArray(validatorFactory.getViolations(user, PutUserValidator.class));

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }

        if (userService.updateUserData(userId, type, updateValue)) {
            return jacksonFactory.createObjectNode().put("status", "successful user profile is updated").asBytes();
        }

        return jacksonFactory.createObjectNode().put("status", "unsuccessful user profile updation failed").asBytes();
    }
}