package org.swiggy.user.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import org.swiggy.user.controller.OrderController;
import org.swiggy.user.model.Address;
import org.swiggy.user.model.Cart;
import org.swiggy.user.model.Order;
import org.swiggy.user.model.User;
import org.swiggy.restaurant.model.Restaurant;
import org.swiggy.common.view.CommonView;
import org.swiggy.common.view.CommonViewImpl;

/**
 * <p>
 * Handles the food orders by the users
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class OrderView {

    private static OrderView orderView;
    private final CommonView commonView;
    private final Logger logger;
    private final CartView cartView;
    private final RestaurantDisplayView restaurantDisplayView;
    private final OrderController orderController;

    private OrderView() {
        logger = LogManager.getLogger(OrderView.class);
        commonView = CommonViewImpl.getInstance();
        restaurantDisplayView = RestaurantDisplayView.getInstance();
        cartView = CartView.getInstance();
        orderController = OrderController.getInstance();
    }

    /**
     * <p>
     * Gets the object of the order view class.
     * </p>
     *
     * @return The order view object
     */
    public static OrderView getInstance() {
        if (null == orderView) {
            orderView = new OrderView();
        }

        return orderView;
    }

    /**
     * <p>
     * Places the order of the user selected items.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant} selected by the user
     */
    public void placeOrder(final long userId, final long restaurantId) {
        final List<Cart> cartList = cartView.getCartList(userId);

        if (!cartList.isEmpty()) {
            logger.info("Select Address\n1.From Previous Oder\n2.New Address");
            long addressId = 0;
            final int userChoice = commonView.getValue();

            if (-1 == userChoice) {
                cartView.displayCartMenu(userId, restaurantId, cartList);
            }

            switch (userChoice) {
                case 1:
                    addressId = displayAddress(userId);
                    break;
                case 2:
                    addressId = getDeliveryAddress(userId);
                    break;
                default:
                    logger.info("Enter A Valid Input");
            }
            final List<Order> orderList = getOrders(userId, cartList, addressId);

            if (orderController.placeOrder(orderList)) {
                logger.info("\nYour Order Is Placed..\nWill Shortly Delivered To Your Address");
                cartView.displayRestaurantsOrLogout(userId);
            }
        } else {
            handleEmptyCart(userId);
        }
    }

    /**
     * <p>
     * Gets the orders placed by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param cartList Represents the list of cart items
     * @param addressId Represents the id of the user chosen address
     * @return orderList Represents the list of order items
     */
    private List<Order> getOrders(final long userId, final List<Cart> cartList, final long addressId) {
        final List<Order> orderList = new ArrayList<>();

        for (final Cart cartItem : cartList) {
            final Order order = new Order();

            order.setCartId(cartItem.getId());
            order.setUser_id(userId);
            order.setRestaurantId(cartItem.getRestaurantId());
            order.setFoodId(cartItem.getFoodId());
            order.setQuantity(cartItem.getQuantity());
            order.setAmount(cartItem.getAmount());
            order.setAddressId(addressId);
            orderList.add(order);
        }

        return orderList;
    }

    /**
     * <p>
     * Displays all the addresses of the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    private long displayAddress(final long userId) {
        final List<Address> addressList = orderController.getAddress(userId);

        if (addressList.isEmpty()) {
            logger.info("You Didn't Have Any Previous Order Addresses");

            return getDeliveryAddress(userId);
        } else {
            for (final Address address : addressList) {
                logger.info(String.format("%d %s %s %s %s %s", addressList.indexOf(address) + 1,
                        address.getHouseNumber(), address.getStreetName(), address.getAreaName(), address.getCityName(),
                        address.getPincode()));
            }
            logger.info("Enter The Delivery Address Id");
            final int index = commonView.getValue() - 1;

            return addressList.get(index).getId();
        }
    }

    /**
     * <p>
     * Gets the delivery address of the current user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @return The address of the current user
     */
    private long getDeliveryAddress(final long userId) {
        final Address address = new Address();

        logger.info("""
                Fill Your Address
                Enter Your House Number""");
        final String houseNumber = commonView.getInfo();

        logger.info("Enter Your Street Name");
        final String streetName = commonView.getInfo();

        logger.info("Enter Your Area Name");
        final String areaName = commonView.getInfo();

        logger.info("Enter Your City Name");
        final String cityName = commonView.getInfo();

        logger.info("Enter Your Pin Code");
        final String pinCode = commonView.getInfo();

        address.setHouseNumber(houseNumber);
        address.setStreetName(streetName);
        address.setAreaName(areaName);
        address.setCityName(cityName);
        address.setPincode(pinCode);
        address.setUserId(userId);
        orderController.addAddress(address);

        return address.getId();
    }

    /**
     * <p>
     * Handles the user cart when the cart is empty.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    private void handleEmptyCart(final long userId) {
        logger.info("""
                Your Order Is Empty
                Please Select A option From Below:
                1.To Order Foods
                2.Logout""");
        final int userChoice = commonView.getValue();

        if (-1 == userChoice) {
            restaurantDisplayView.displayRestaurants(userId);
        }

        switch (userChoice) {
            case 1:
                restaurantDisplayView.displayRestaurants(userId);
                break;
            case 2:
                logger.info("Your Account Is Logged Out");
                UserView.getInstance().displayMainMenu();
                break;
            default:
                logger.warn("Enter A Valid Option");
                handleEmptyCart(userId);
        }
    }

    /**
     * <p>
     * Displays the orders placed by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    public void displayOrders(final long userId) {
        final List<Order> orders = orderController.getOrders(userId);

        if (orders.isEmpty()) {
            logger.info("No Placed Orders");
        } else {
            logger.info("""
                    Your Orders
                    Name| Food | Quantity | Amount""");

            for (final Order order : orders) {
                logger.info(String.format("%s %s %d %.2f", order.getRestaurantName(), order.getFoodName(),
                        order.getQuantity(), order.getAmount()));
            }
        }
    }
}
