package org.swiggy.user.internal.restcontroller;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
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
import org.swiggy.user.model.Address;
import org.swiggy.user.model.Order;
import org.swiggy.user.model.User;
import org.swiggy.user.internal.service.OrderService;
import org.swiggy.user.internal.service.impl.OrderServiceImpl;
import org.swiggy.validator.hibernatevalidator.ValidatorFactory;
import org.swiggy.validator.validatorgroup.address.PostAddressValidator;
import org.swiggy.validator.validatorgroup.address.GetAddressValidator;
import org.swiggy.validator.validatorgroup.order.GetOrderValidator;
import org.swiggy.validator.validatorgroup.order.PostOrderValdiator;

/**
 * <p>
 * Handles the order related operation and responsible for receiving user input through rest api and processing it.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
@Path("/order")
public class OrderController {

    private static OrderController orderController;
    private final OrderService orderService;
    private final JacksonFactory jacksonFactory;
    private final Validator validator;
    private final ValidatorFactory validatorFactory;

    private OrderController() {
        orderService = OrderServiceImpl.getInstance();
        jacksonFactory = JacksonFactory.getInstance();
        validator = Validation.byProvider(HibernateValidator.class).configure()
                .messageInterpolator(new ParameterMessageInterpolator()).buildValidatorFactory().getValidator();
        validatorFactory = ValidatorFactory.getInstance();
    }

    /**
     * <p>
     * Gets the object of the cart controller class.
     * </p>
     *
     * @return The cart controller object
     */
    public static OrderController getInstance() {
        if (null == orderController) {
            orderController = new OrderController();
        }

        return orderController;
    }

    /**
     * <p>
     * places the user orders.
     * </p>
     *
     * @param orderList Represents the list of order items
     * @return byte array of json object
     */
    @POST
    @Consumes("application/json")
    public byte[] placeOrder(final List<Order> orderList) {
        final Set<ConstraintViolation<List<Order>>> violationSet = validator.validate(orderList,
                PostOrderValdiator.class);

        if (!violationSet.isEmpty()) {
            final JsonArray jsonViolations = jacksonFactory.createArrayNode();

            for (final ConstraintViolation violation : violationSet) {
                jsonViolations.add(jacksonFactory.createObjectNode().put(violation.getPropertyPath().toString(), violation.getMessage()));
            }

            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jacksonFactory.createObjectNode();

        if (orderService.placeOrder(orderList)) {
            return jsonObject.put("status", "successful order was placed").asBytes();
        }

        return jsonObject.put("status", "unsuccessful order placing failed").asBytes();
    }

    /**
     * <p>
     * Stores the address of the user.
     * </p>
     *
     * @param address Represents the address of the user
     * @return byte array of json object
     */
    @Path("/address")
    @POST
    @Consumes("application/json")
    public byte[] addAddress(final Address address) {
        final Set<ConstraintViolation<Address>> violationSet = validator.validate(address,
                PostAddressValidator.class);

        if (!violationSet.isEmpty()) {
            final JsonArray jsonViolations = jacksonFactory.createArrayNode();

            for (final ConstraintViolation violation : violationSet) {
                jsonViolations.add(jacksonFactory.createObjectNode().put(violation.getPropertyPath().toString(), violation.getMessage()));
            }

            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jacksonFactory.createObjectNode();

        if (orderService.addAddress(address)) {
            return jsonObject.put("status", "successful address was added").asBytes();
        }

        return jsonObject.put("status", "successful adding address was failed").asBytes();
    }

    /**
     * <p>
     * Displays all the addresses of the user.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return byte array of json object
     */
    @Path("/address/{userId}")
    @GET
    @Produces("application/json")
    public byte[] getAddress(@PathParam("userId") final long userId) {
        final Address address = new Address();

        address.setUserId(userId);
        final JsonArray jsonViolations = validatorFactory.getViolations(address, GetAddressValidator.class);
//        final Set<ConstraintViolation<Address>> violationSet = validator.validate(address,
//                GetAddressValidator.class);
//
//        if (!violationSet.isEmpty()) {
//            final JsonArray jsonViolations = jacksonFactory.createArrayNode();
//
//            for (final ConstraintViolation violation : violationSet) {
//                jsonViolations.add(jacksonFactory.createObjectNode().put(violation.getPropertyPath().toString(), violation.getMessage()));
//            }
//
//            return jsonViolations.asBytes();
//        }

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final List<Address> addressList = orderService.getAddress(userId);

        if (null == addressList) {
            return jacksonFactory.createObjectNode().put("status", "unsuccessful address list is empty or user id is invalid").asBytes();
        }

        return jacksonFactory.createArrayNode().build(addressList).asBytes();
    }

    /**
     * <p>
     * Gets the orders placed by the user.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return byte array of json object
     */
    @Path("/{userId}")
    @GET
    @Produces("application/json")
    public byte[] getOrders(@PathParam("userId") final long userId) {
        final Order order = new Order();

        order.setUserId(userId);
        final Set<ConstraintViolation<Order>> violationSet = validator.validate(order,
                GetOrderValidator.class);

        if (!violationSet.isEmpty()) {
            final JsonArray jsonViolations = jacksonFactory.createArrayNode();

            for (final ConstraintViolation violation : violationSet) {
                jsonViolations.add(jacksonFactory.createObjectNode().put(violation.getPropertyPath().toString(), violation.getMessage()));
            }

            return jsonViolations.asBytes();
        }
        final List<Order> orderList = orderService.getOrders(userId);

        if (null == orderList) {
            return jacksonFactory.createObjectNode().put("status", "unsuccessful order list is empty or user id is invalid").asBytes();
        }

        return jacksonFactory.createArrayNode().build(orderList).asBytes();
    }
}