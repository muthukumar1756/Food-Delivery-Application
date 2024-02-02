package org.swiggy.view;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.controller.CartController;
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
     * @param user Represents the current {@link User}
     */
    public void displayRestaurants(final User user) {
        restaurantView.displayRestaurants();
        GetRestaurantAndMenucard(user);
    }

    /**
     * <p>
     * Gets the selection of a restaurant by the user.
     * </p>
     *
     * @param user Represents the current {@link User}
     */
    private void GetRestaurantAndMenucard(final User user) {
        final int restaurantNumber = getChoice();

        if (-1 == restaurantNumber) {
            UserView.getInstance().displayHomePageMenu(user);
        }
        final Restaurant restaurant = restaurantView.getRestaurant(restaurantNumber);

        if (null == restaurant) {
            logger.warn("Select A Valid Restaurant Id");
            displayRestaurants(user);
        }
        final List<Food> menucard = restaurantView.getMenucard(restaurant);

        if (null == menucard) {
            logger.warn("The Chosen Restaurant Currently Doesn't Have Any Available Items");
            displayRestaurants(user);
        }
        displayMenucard(restaurant, user, menucard);
    }

    /**
     * <p>
     * Displays the menucard of the selected restaurant.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param user Represents the current {@link User}
     * @param menucard Represents the menucard of the selected restaurant
     */
    public void displayMenucard(final Restaurant restaurant, final User user, final List<Food> menucard) {
        restaurantView.displayMenucard(menucard);
        selectFilter(restaurant, user, menucard);
    }

    /**
     * <p>
     * Displays and handles the users choice to filter foods or continue with food ordering.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param user Represents the current {@link User}
     * @param menucard Represents the menucard of the selected restaurant
     */
    private void selectFilter(final Restaurant restaurant, final User user, final List<Food> menucard) {
        logger.info("\n1.To Apply Filter\n2.To Continue Food Ordering\n3.To Select Other Restaurant");
        final int userChoice = getChoice();

        if (-1 == userChoice) {
            displayRestaurants(user);
        }

        switch (userChoice) {
            case 1:
                selectFoodFilter(restaurant, user, menucard);
                break;
            case 2:
                selectFood(restaurant, user, menucard);
                break;
            case 3:
                displayRestaurants(user);
                break;
            default:
                logger.warn("Invalid Option Try Again\n");
                displayMenucard(restaurant, user, menucard);
        }
    }

    /**
     * <p>
     * Displays and handles the users choice to get veg or nonveg food.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param user Represents the current {@link User}
     * @param menucard Represents the menucard of the selected restaurant
     */
    private void selectFoodFilter(final Restaurant restaurant, final User user, final List<Food> menucard) {
        logger.info("\nFilter Type\n1.Veg\n2.Non-Veg");
        final int filterTypeOption = getChoice();

        if (-1 == filterTypeOption) {
            displayMenucard(restaurant, user, menucard);
        }

        switch (filterTypeOption) {
            case 1:
                final List<Food> vegMenucard = restaurantView.getVegMenucard(restaurant);
                restaurantView.displayMenucard(vegMenucard);
                selectFood(restaurant, user, vegMenucard);
                break;
            case 2:
                final List<Food> nonVegMenucard = restaurantView.getNonVegMenucard(restaurant);
                restaurantView.displayMenucard(nonVegMenucard);
                selectFood(restaurant, user, nonVegMenucard);
                break;
            default:
                logger.warn("Enter valid option");
                selectFoodFilter(restaurant, user, menucard);
        }
    }

    /**
     * <p>
     * Gets the selection of food by the user.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param user Represents the current {@link User}
     * @param menucard Represents the menucard of the selected restaurant
     */
    private void selectFood(final Restaurant restaurant, final User user, final List<Food> menucard) {
        logger.info("Enter FoodId To Add To Cart");
        final int selectedIndex = getChoice();

        if (-1 == selectedIndex) {
            displayMenucard(restaurant, user, menucard);
        }
        final int foodNumber = selectedIndex - 1;

        if (foodNumber >= 0 && foodNumber <= menucard.size()) {
            final Food selectedFood = menucard.get(foodNumber);
            final int quantity = getQuantity(user, selectedFood);

            addFoodToCart(user, restaurant, selectedFood, quantity);
        } else {
            logger.warn("Enter A Valid Option From The Menucard");
            displayMenucard(restaurant, user, menucard);
        }
        addFoodOrPlaceOrder(restaurant, user);
    }

    /**
     * <p>
     * Adds the selected food to the user cart.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param food Represents the {@link Food} selected by user
     * @param quantity Represents the quantity of selected food by the user
     */
    private void addFoodToCart(final User user, final Restaurant restaurant, final Food food, final int quantity) {
        if (!CartView.getInstance().addFoodToCart(food, user, quantity, restaurant.getRestaurantId())) {
            handleFoodsFromVariousRestaurants(user, restaurant, food, quantity);
        }
    }

    /**
     * <p>
     * Gets the quantity of selected food by the user.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @param food Represents the {@link Food} selected by user
     */
    private int getQuantity(final User user, final Food food) {
        logger.info("Enter The Quantity");
        final int quantity = getChoice();

        if (-1 == quantity) {
            displayRestaurants(user);
        }
        final int foodQuantity = restaurantView.getQuantity(food, quantity);
        final int availableQuantity = foodQuantity - quantity;

        if (0 > availableQuantity) {
            logger.info("The Entered Quantity Is Not Available");
            getQuantity(user, food);
        }

        return quantity;
    }

    /**
     * <p>
     * Handles the condition of user cart having foods from single restaurant.
     * </p>
     *
     * @param user Represents the current {@link User}
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param food Represents the {@link Food} selected by user
     * @param quantity Represents the quantity of selected food by the user
     */
    private void handleFoodsFromVariousRestaurants(final User user, final Restaurant restaurant, final Food food,
                                                   final int quantity) {
        logger.warn("""
                    Your Cart Contains Items From Other Restaurant!.
                    Would You Like To Reset Your Cart For Adding Items From This Restaurant ?
                    1 To Reset Cart
                    2 To Cancel""");
        final int userChoice = getChoice();

        if (1 == (userChoice)) {
            CartController.getInstance().clearCart(user);
            addFoodToCart(user, restaurant, food, quantity);
        }
    }

    /**
     * <p>
     * Displays and handles the user choice to add extra foods or to place order.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param user Represents the current {@link User}
     */
    public void addFoodOrPlaceOrder(final Restaurant restaurant, final User user) {
        logger.info("Do You Want To Add More Food\n1.Add More Food \n2.Place Order");
        final int userChoice = getChoice();

        if (-1 == userChoice) {
            displayMenucard(restaurant, user, restaurant.getMenuCard());
        }

        switch (userChoice) {
            case 1:
                displayMenucard(restaurant, user, restaurant.getMenuCard());
                break;
            case 2:
                CartView.getInstance().displayCart(restaurant, user);
                break;
            default:
                logger.warn("Enter A Valid Option");
                addFoodOrPlaceOrder(restaurant, user);
        }
    }
}
