package org.swiggy.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Map;

import org.swiggy.controller.CartController;
import org.swiggy.controller.RestaurantController;
import org.swiggy.model.Food;
import org.swiggy.model.Restaurant;
import org.swiggy.model.User;

/**
 * <p>
 * Displays restaurants details and menucard of the selected restaurant.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class RestaurantView extends CommonView {

    private static RestaurantView restaurantView;

    private final Logger logger;
    private final RestaurantController restaurantController;

    private RestaurantView() {
        logger = LogManager.getLogger(RestaurantView.class);
        restaurantController = RestaurantController.getInstance();
    }

    /**
     * <p>
     * Gets the object of the restaurant view class.
     * </p>
     *
     * @return The restaurant view object
     */
    public static RestaurantView getInstance() {
        if (null == restaurantView) {
            restaurantView = new RestaurantView();
        }

        return restaurantView;
    }

    /**
     * <p>
     * Displays the available restaurants.
     * </p>
     * @param user Represents the current {@link User}
     */
    public void displayRestaurants(final User user) {
        logger.info("To Go Back Enter *\nAvailable Restaurants In Your Area:");

        for (final Map.Entry<Integer, Restaurant> restaurants : restaurantController.getRestaurants().entrySet()) {
            logger.info(String.format("%d %s", restaurants.getKey(), restaurants.getValue().getName()));
        }
        selectRestaurant(user);
    }

    /**
     * <p>
     * Gets the selection of a restaurant by the user.
     * </p>
     *
     * @param user Represents the current {@link User}
     */
    private void selectRestaurant(final User user) {
        final int restaurantNumber = getChoice();

        if (-1 == restaurantNumber) {
            UserView.getInstance().displayHomePageMenu(user);
        }
        final Restaurant selectedRestaurant = restaurantController.getRestaurants().get(restaurantNumber);

        if (null == selectedRestaurant) {
            logger.warn("Select A Valid Restaurant Id");
            displayRestaurants(user);
        }
        final List<Food> selectedMenuCard = restaurantController.getMenuCard(selectedRestaurant);

        displayMenuCard(selectedRestaurant, selectedMenuCard, user);
    }

    /**
     * <p>
     * Displays the menucard of the restaurant selected by the user.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param selectedMenuCard Represents the menucard of the selected restaurant
     * @param user Represents the current {@link User}
     */
    private void displayMenuCard(final Restaurant restaurant,final List<Food> selectedMenuCard, final User user) {
        if (null != selectedMenuCard) {
            logger.info("\nAvailable Items:\n");
            logger.info("ID | Name | Rate | Category ");

            for (final Food food : selectedMenuCard) {
                logger.info(String.format("%d %s %.2f %s", selectedMenuCard.indexOf(food) + 1,
                        food.getFoodName(), food.getRate(), food.getType()));
            }
            selectFilter(restaurant, user, selectedMenuCard);
        } else {
            logger.warn("The Chosen Restaurant Currently Doesn't Have Any Available Items");
            displayRestaurants(user);
        }
    }

    /**
     * <p>
     * Displays and handles the users choice to filter foods or continue with food ordering.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param user Represents the current {@link User}
     * @param menuCard Represents the menucard from the selected restaurant
     */
    private void selectFilter(final Restaurant restaurant, final User user, final List<Food> menuCard) {
        logger.info("\n1.To Apply Filter\n2.To Continue Food Ordering\n3.To Select Other Restaurant");
        final int userChoice = getChoice();

        if (-1 == userChoice) {
            UserView.getInstance().displayHomePageMenu(user);
        }

        switch (userChoice) {
            case 1:
                selectFoodFilter(restaurant, user, menuCard);
                break;
            case 2:
                selectFood(restaurant, user, menuCard);
                break;
            case 3:
                displayRestaurants(user);
                break;
            default:
                logger.warn("Invalid Option Try Again\n");
                displayMenuCard(restaurant, menuCard, user);
        }
    }

    /**
     * <p>
     * Gets the selection of food by the user.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param user Represents the current {@link User}
     * @param menuCard Represents the menucard from the selected restaurant
     */
    private void selectFood(final Restaurant restaurant, final User user, final List<Food> menuCard) {
        logger.info("Enter FoodId To Add To Cart");
        final int selectedIndex = getChoice();

        if (-1 == selectedIndex) {
            displayRestaurants(user);
        }
        final int foodNumber = selectedIndex - 1;

        if (foodNumber >= 0 && foodNumber <= menuCard.size()) {
            final Food selectedFood = menuCard.get(foodNumber);
            final int quantity = getQuantity(user, selectedFood);

            addFoodToCart(user, restaurant, selectedFood, quantity);
        } else {
            logger.warn("Enter A Valid Option From The Menucard");
            displayMenuCard(restaurant, menuCard, user);
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
    private void addFoodToCart(final User user, final Restaurant restaurant, final Food food,
                              final int quantity) {
        if (!CartController.getInstance().addFoodToCart(food, user, quantity,restaurant.getRestaurantId())) {
            handleFoodsFromVariousRestaurants(user, restaurant, food, quantity);
        }
    }

    /**
     * <p>
     * Gets the quantity of selected food by the user.
     * </p>
     *
     * @param user Represents the current {@link User}
     */
    private int getQuantity(final User user, final Food food) {
        logger.info("Enter The Quantity");
        final int quantity = getChoice();

        if (-1 == quantity) {
            displayRestaurants(user);
        }
        final int foodQuantity = restaurantController.getQuantity(food, quantity);
        final int availableQuantity = foodQuantity - quantity;

        if (0 > availableQuantity) {
            logger.info("The Entered Quantity Is Not Available");
            getQuantity(user,food);
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
                    1 To Reset
                    2 To Cancel""");
        final int userChoice = getChoice();

        if (1 == (userChoice)) {
            CartController.getInstance().clearCart(user);
            addFoodToCart(user, restaurant, food, quantity);
        }
    }

    /**
     * <p>
     * Displays and handles the users choice to get veg or nonveg food.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param user Represents the current {@link User}
     * @param menuCard Represents the menucard from the selected restaurant
     */
    private void selectFoodFilter(final Restaurant restaurant, final User user, final List<Food> menuCard) {
        logger.info("\nFilter Type\n1.Veg\n2.Non-Veg");
        final int filterTypeOption = getChoice();

        if (-1 == filterTypeOption) {
            selectFilter(restaurant, user, menuCard);
        }

        switch (filterTypeOption) {
            case 1:
                selectVegFood(restaurant, user);
                break;
            case 2:
                selectNonVegFood(restaurant, user);
                break;
            default:
                logger.warn("Enter valid option");
                selectFoodFilter(restaurant, user, menuCard);
        }
    }

    /**
     * <p>
     * Displays the veg foods from the selected restaurant.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param user Represents the current {@link User}
     */
    private void selectVegFood(final Restaurant restaurant, final User user) {
        final List<Food> selectedMenuCard = restaurant.getVegMenuCard();

        logger.info("Available Items:");
        for (final Food food : selectedMenuCard) {
            logger.info(String.format("%d %s %.2f %s", selectedMenuCard.indexOf(food) + 1,
                    food.getFoodName(), food.getRate(), food.getType()));
        }
        selectFood(restaurant, user, selectedMenuCard);
    }

    /**
     * <p>
     * Displays the nonveg foods from the selected restaurant.
     * </p>
     *
     * @param restaurant Represents the {@link Restaurant} selected by the user
     * @param user Represents the current {@link User}
     */
    private void selectNonVegFood(final Restaurant restaurant, final User user) {
        final List<Food> selectedRestaurantMenuCard = restaurant.getNonVegMenuCard();

        logger.info("\nAvailable Items:");

        for (final Food food : selectedRestaurantMenuCard) {
            logger.info(String.format("%d %s %.2f %s", selectedRestaurantMenuCard.indexOf(food) + 1,
                    food.getFoodName(), food.getRate(), food.getType()));
        }
        selectFood(restaurant, user, selectedRestaurantMenuCard);
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
            displayMenuCard(restaurant, restaurant.getMenuCard(), user);
        }

        switch (userChoice) {
            case 1:
                displayMenuCard(restaurant, restaurant.getMenuCard(), user);
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