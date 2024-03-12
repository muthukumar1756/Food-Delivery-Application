package org.swiggy.user.service.impl2;

import org.swiggy.user.datahandler.OrderDataHandler;
import org.swiggy.user.datahandler.impl.OrderDataHandlerImpl;
import org.swiggy.user.model.Address;
import org.swiggy.user.model.Order;
import org.swiggy.user.model.User;
import org.swiggy.user.service.OrderService;

import java.util.List;

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
    private final OrderDataHandler orderDataHandler;

    private OrderServiceImpl() {
        orderDataHandler = OrderDataHandlerImpl.getInstance();
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

    /**
     * {@inheritDoc}
     *
     * @param orderList Represents the list of order items
     * @return True if the order is placed, false otherwise
     */
    @Override
    public boolean placeOrder(final List<Order> orderList) {
        return orderDataHandler.placeOrder(orderList);
    }

    /**
     * {@inheritDoc}
     *
     * @param address Represents the address of the user
     * @return True if the address is added, false otherwise
     */
    @Override
    public boolean addAddress(final Address address) {
        return orderDataHandler.addAddress(address);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the current {@link User}
     * @return List of addresses of the user
     */
    public List<Address> getAddress(final long userId) {
        return orderDataHandler.getAddress(userId);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the current {@link User}
     * @return List having all the orders placed by the user
     */
    @Override
    public List<Order> getOrders(final long userId) {
        return orderDataHandler.getOrders(userId);
    }
}
