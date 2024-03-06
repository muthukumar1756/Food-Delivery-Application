package org.swiggy.view;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.controller.CartController;
import org.swiggy.model.Cart;
import org.swiggy.model.Food;
import org.swiggy.model.Restaurant;
import org.swiggy.model.User;

public class RestaurantDisplayView extends CommonView {

    private static RestaurantDisplayView restaurantDisplayView;
    private final RestaurantView restaurantView;
    private final Logger logger;

    private RestaurantDisplayView() {
        logger = LogManager.getLogger(RestaurantDisplayView.class);
        restaurantView = org.swiggy.view.RestaurantView.getInstance();
    }

    /**
     * <p>
     * Gets the object of the restaurant view class.
     * </p>
     *
     * @return The restaurant view object
     */
    public static RestaurantDisplayView getInstance() {
        if (null == restaurantDisplayView) {
            restaurantDisplayView = new RestaurantDisplayView();
        }

        return restaurantDisplayView;
    }

    /**
     * <p>
     * Displays the available restaurants.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    public void displayRestaurants(final long userId) {
        logger.info("""
                    To Go Back Enter *
                    Available Restaurants In Your Area:""");
        final List<Restaurant> restaurantList = restaurantView.getRestaurants();

        for (final Restaurant restaurant : restaurantList) {
            logger.info(String.format("%d %s", restaurantList.indexOf(restaurant) + 1, restaurant.getName()));
        }
        getRestaurant(userId, restaurantList);
    }

    /**
     * <p>
     * Gets the selection of a restaurant by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    private void getRestaurant(final long userId, final List<Restaurant> restaurantList) {
        final int restaurantNumber = getValue();

        if (-1 == restaurantNumber) {
            UserView.getInstance().displayHomePageMenu(userId);
        }

        if (0 <= restaurantNumber && restaurantList.size() >= restaurantNumber) {
            final Restaurant restaurant = restaurantList.get(restaurantNumber - 1);

            if (null == restaurant) {
                logger.warn("Select A Valid Restaurant Id");
                displayRestaurants(userId);
            }

            getMenucard(userId, restaurant.getRestaurantId());
        } else {
            logger.warn("Select The Valid Option");
            displayRestaurants(userId);
        }
    }

    /**
     * <p>
     * Gets the menucard according to the user chosen food category.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the current {@link Restaurant}
     */
    private void getMenucard(final long userId, final long restaurantId) {
        logger.info("""
                    Select Food Type
                    1.VEG
                    2.NONVEG
                    3.VEG & NONVEG""");
        final int foodType = getValue();

        if (-1 == foodType) {
            displayRestaurants(userId);
        }

        if (0 < foodType && 4 > foodType) {
            final List<Food> menucard = restaurantView.getMenucard(restaurantId, foodType);

            if (null == menucard) {
                logger.warn("The Chosen Restaurant Currently Doesn't Have Any Available Items");
                displayRestaurants(userId);
            }
            displayFoods(menucard);
            selectFood(userId, restaurantId, menucard);
        } else {
            logger.warn("Enter A Valid Option");
            getMenucard(userId, restaurantId);
        }
    }

    /**
     * <p>
     * Displays the menucard of the restaurant selected by the user.
     * </p>
     *
     * @param menucard Represents the menucard from the selected restaurant
     */
    public void displayFoods(final List<Food> menucard) {
        logger.info("""
                    Available Items:
                    ID | Name | Rate | Category""");

        for (final Food food : menucard) {
            logger.info(String.format("%d %s %.2f %s", menucard.indexOf(food) + 1,
                    food.getFoodName(), food.getRate(), food.getType()));
        }
    }

    /**
     * <p>
     * Gets the selection of food by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the current {@link Restaurant}
     * @param menucard Represents the menucard of the selected restaurant
     */
    private void selectFood(final long userId, final long restaurantId, final List<Food> menucard) {
        logger.info("Enter FoodId To Add To Cart");
        final int selectedIndex = getValue();

        if (-1 == selectedIndex) {
            getMenucard(userId, restaurantId);
        }
        final int foodNumber = selectedIndex - 1;

        if (0 <= foodNumber && menucard.size() >= foodNumber) {
            final Food selectedFood = menucard.get(foodNumber);
            final int quantity = getQuantity(userId, selectedFood.getFoodId());

            addFoodToCart(userId, restaurantId, selectedFood, quantity);
        } else {
            logger.warn("Enter A Valid Option From The Menucard");
            selectFood(userId, restaurantId, menucard);
        }
        addFoodOrPlaceOrder(userId, restaurantId);
    }

    /**
     * <p>
     * Adds the selected food to the user cart.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant}
     * @param food Represents the {@link Food} selected by user
     * @param quantity Represents the quantity of selected food by the user
     */
    private void addFoodToCart(final long userId, final long restaurantId, final Food food, final int quantity) {
        final Cart cart = new Cart();

        cart.setUserId(userId);
        cart.setRestaurantId(restaurantId);
        cart.setFoodId(food.getFoodId());
        cart.setQuantity(quantity);
        cart.setAmount(food.getRate() * quantity);

        if (!CartView.getInstance().addFoodToCart(cart)) {
            handleFoodsFromVariousRestaurants(userId, restaurantId, food, quantity);
        }
    }

    /**
     * <p>
     * Handles the condition of user cart having foods from single restaurant.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant}
     * @param food Represents the {@link Food} selected by user
     * @param quantity Represents the quantity of selected food by the user
     */
    private void handleFoodsFromVariousRestaurants(final long userId, final long restaurantId, final Food food,
                                                   final int quantity) {
        logger.warn("""
                    Your Cart Contains Items From Other Restaurant!.
                    Would You Like To Reset Your Cart For Adding Items From This Restaurant ?
                    1 To Reset Cart
                    2 To Cancel""");
        final int userChoice = getValue();

        switch (userChoice) {
            case 1:
                CartController.getInstance().clearCart(userId);
                addFoodToCart(userId, restaurantId, food, quantity);
                break;
            case 2:
                logger.info("Cancelled");
                break;
            default:
                logger.info("Enter a Valid Input");
                break;
        }
    }

    /**
     * <p>
     * Gets the quantity of selected food by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param foodId Represents the id of the current {@link Food} selected by user
     */
    private int getQuantity(final long userId, final long foodId) {
        logger.info("Enter The Quantity");
        final int quantity = getValue();

        if (-1 == quantity) {
            displayRestaurants(userId);
        }
        final int foodQuantity = restaurantView.getQuantity(foodId);
        final int availableQuantity = foodQuantity - quantity;

        if (0 > availableQuantity) {
            logger.info("The Entered Quantity Is Not Available");
            getQuantity(userId, foodId);
        }

        return quantity;
    }

    /**
     * <p>
     * Displays and handles the user choice to add extra foods or to place order.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant}
     */
    public void addFoodOrPlaceOrder(final long userId, final long restaurantId) {
        logger.info("""
                    Do You Want To Add More Food
                    1.Add More Food
                    2.Place Order""");
        final int userChoice = getValue();

        if (-1 == userChoice) {
            getMenucard(userId, restaurantId);
        }

        switch (userChoice) {
            case 1:
                getMenucard(userId, restaurantId);
                break;
            case 2:
                CartView.getInstance().displayCart(userId, restaurantId);
                break;
            default:
                logger.warn("Enter A Valid Option");
                addFoodOrPlaceOrder(userId, restaurantId);
        }
    }
}
