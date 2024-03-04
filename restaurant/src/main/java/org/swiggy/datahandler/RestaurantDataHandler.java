package org.swiggy.datahandler;

import org.swiggy.model.Food;
import org.swiggy.model.Restaurant;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Provides data base service for the restaurant
 * </p>
 *
 * @author Muthu kumar v
 * @version 1.1
 */
public interface RestaurantDataHandler {

     /**
      * <p>
      * Creates the new restaurant user.
      * </p>
      *
      * @param restaurant Represents the current {@link Restaurant}
      * @return True if restaurant user is created, false otherwise
      */
     boolean createRestaurantProfile(final Restaurant restaurant);

     /**
      * <p>
      * Gets the restaurant if the phone_number and password matches.
      * </p>
      *
      * @param phoneNumber Represents the phone_number of the current restaurant user
      * @param password Represents the password of the current restaurant user
      * @return The current user
      */
     Restaurant getRestaurant(final String phoneNumber, final String password);

     /**
      * <p>
      * Gets the restaurant user if the id matches.
      * </p>
      *
      * @param restaurantId Represents the password of the current user
      * @return The current restaurant user
      */
     Restaurant getRestaurantById(final long restaurantId);

     /**
      * <p>
      * Loads all the restaurants details.
      * </p>
      *
      * @param restaurants Represents all the {@link Restaurant}
      */
     boolean loadRestaurantsData(final List<Restaurant> restaurants);

     /**
      * <p>
      * Loads the food details given from restaurant.
      * </p>
      *
      * @param food Represents the current food added by the restaurant
      * @param restaurantId Represents the id of the Restaurant
      */
     void loadFoodData(final Food food, final long restaurantId);

     /**
      * <p>
      * Creates the menucard for the restaurant.
      * </p>
      *
      * @param menuCard Contains the list of foods in the restaurant
      */
     void loadMenuCardData(final Map<Food, Integer> menuCard);

     /**
      * <p>
      * Gets all the restaurants
      * </p>
      *
      * @return The map having all the restaurants.
      */
     List<Restaurant> getRestaurants();

     /**
      * <p>
      * Gets the available food quantity in the restaurant .
      * </p>
      *
      * @param foodId Represents the id of the current {@link Food} selected by the user
      */
     int getQuantity(final long foodId);

     /**
      * <p>
      * Gets the menucard of the selected restaurant by the user.
      * </p>
      *
      * @param restaurantId Represents the id of the Restaurant
      * @param foodTypeId Represents the id of the food type.
      * @return The menucard list
      */
     List<Food> getMenuCard(final long restaurantId, final int foodTypeId);


     /**
      * <p>
      * Removes the food from the restaurant.
      * </p>
      *
      * @param foodId Represents the id of the current {@link Food} selected by the user
      */
     void removeFood(final long foodId);

     /**
      * <p>
      * Updates the data of the current user.
      * </p>
      *
      * @param restaurantId Represents the id 0f the current {@link Restaurant}
      * @param restaurantData Represents the data of the current user to be updated
      * @param type Represents the type of data of the current user to be updated
      */
     void updateRestaurantData(final long restaurantId, final String type, final String restaurantData);
}
