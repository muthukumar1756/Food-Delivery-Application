package org.swiggy.service.impl2;

import org.swiggy.datahandler.OrderDataHandler;
import org.swiggy.datahandler.impl.OrderDataHandlerImpl;
import org.swiggy.model.Address;
import org.swiggy.model.Cart;
import org.swiggy.model.Order;
import org.swiggy.model.User;
import org.swiggy.service.OrderService;

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
     * @param cartList Represents the list of cart items
     * @param orderList Represents the list of orders
     * @return True if the order is placed, false otherwise
     */
    @Override
    public boolean placeOrder(final List<Cart> cartList, List<Order> orderList) {
        return orderDataHandler.placeOrder(cartList, orderList);
    }

    /**
     * {@inheritDoc}
     *
     * @param address Represents the address of the user
     */
    @Override
    public void addAddress(final Address address) {
        orderDataHandler.addAddress(address);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the current {@link User}
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
