package org.swiggy.service.impl1;

import org.swiggy.model.*;
import org.swiggy.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Implements the service of the user order related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class OrderServiceImpl implements OrderService {

    private static OrderService orderService;
    private List<Order> orderList;

    private OrderServiceImpl() {
        orderList = new ArrayList<>();
    }

    /**
     * <p>
     * Gets the cart service implementation class object.
     * </p>
     *
     * @return The cart service implementation object
     */
    public static OrderService getInstance() {
        if (null == orderService) {
            orderService = new OrderServiceImpl();
        }

        return orderService;
    }

    @Override
    public boolean placeOrder(List<Cart> cartList, List<Order> orderList) {
        return false;
    }

    @Override
    public void addAddress(Address address) {
    }

    @Override
    public List<Address> getAddress(long userId) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the current {@link User}
     * @return List having all the orders placed by the user
     */
    @Override
    public List<Order> getOrders(final long userId) {
        return null;
    }
}
