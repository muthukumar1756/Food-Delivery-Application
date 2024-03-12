package org.swiggy.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.swiggy.user.exception.AddressDataLoadFailureException;
import org.swiggy.user.exception.OrderDataNotFoundException;
import org.swiggy.user.exception.OrderPlacementFailureException;
import org.swiggy.user.model.Address;
import org.swiggy.user.model.Order;
import org.swiggy.user.model.User;
import org.swiggy.user.service.OrderService;
import org.swiggy.user.service.impl2.OrderServiceImpl;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/order")
public class RestOrderController {

    private static RestOrderController restOrderController;
    private final OrderService orderService;

    private RestOrderController() {
        orderService = OrderServiceImpl.getInstance();
    }

    /**
     * <p>
     * Gets the object of the cart controller class.
     * </p>
     *
     * @return The cart controller object
     */
    public static RestOrderController getInstance() {
        if (null == restOrderController) {
            restOrderController = new RestOrderController();
        }

        return restOrderController;
    }

    /**
     * <p>
     * places the user orders.
     * </p>
     *
     * @param jsonOrder Represents the list of order items
     * @return Response object of the http request
     */
    @Path("/")
    @POST
    @Consumes("application/json")
    public Response placeOrder(final String jsonOrder) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<Order> orderList;

        try {
            orderList = objectMapper.readValue(jsonOrder, new TypeReference<List<Order>>() {});
        } catch (JsonProcessingException exception) {
            throw new OrderPlacementFailureException(exception.getMessage());
        }

        if (orderService.placeOrder(orderList)) {
            return Response.ok("Order Placed").build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Enter A Valid Order List").build();
    }

    /**
     * <p>
     * Stores the address of the user.
     * </p>
     *
     * @param jsonAddress Represents the address of the user
     * @return Response object of the http request
     */
    @Path("/")
    @POST
    @Consumes("application/json")
    public Response addAddress(final String jsonAddress) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode jsonNode;

        try {
            jsonNode = objectMapper.readTree(jsonAddress);
        } catch (JsonProcessingException exception) {
            throw new AddressDataLoadFailureException(exception.getMessage());
        }
        final Address address = new Address();

        address.setUserId(jsonNode.get("userId").asLong());
        address.setHouseNumber(jsonNode.get("houseNumber").asText());
        address.setStreetName(jsonNode.get("streetName").asText());
        address.setAreaName(jsonNode.get("areaName").asText());
        address.setCityName(jsonNode.get("cityName").asText());
        address.setPincode(jsonNode.get("pincode").asText());

        if (orderService.addAddress(address)) {
            return Response.ok("Address Object Added").build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Enter A Valid Address").build();
    }

    /**
     * <p>
     * Displays all the addresses of the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @return Response object of the http request
     */
    @Path("/{userId}")
    @GET
    @Produces("application/json")
    public Response getAddress(@PathParam("userId") final long userId) {
        final List<Address> addresses = orderService.getAddress(userId);

        if (null != addresses) {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonArray;

            try {
                jsonArray = objectMapper.writeValueAsString(addresses);
            } catch (JsonProcessingException exception) {
                throw new AddressDataLoadFailureException(exception.getMessage());
            }

            return Response.ok(jsonArray).build();
        }

        return Response.status(Response.Status.NO_CONTENT).entity("Enter A Valid User Id").build();
    }

    /**
     * <p>
     * Gets the orders placed by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @return Response object of the http request
     */
    @Path("/{userId}")
    @GET
    @Produces("application/json")
    public Response getOrders(@PathParam("userId") final long userId) {
        final List<Order> orderList = orderService.getOrders(userId);

        if (null !=  orderList) {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonArray;

            try {
                jsonArray = objectMapper.writeValueAsString(orderList);
            } catch (JsonProcessingException exception) {
                throw new OrderDataNotFoundException(exception.getMessage());
            }

            return Response.ok(jsonArray).build();
        }

        return Response.noContent().entity("Enter A Valid User Id").build();
    }
}
