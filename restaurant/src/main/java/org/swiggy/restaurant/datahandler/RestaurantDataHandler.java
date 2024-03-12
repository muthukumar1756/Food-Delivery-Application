package org.swiggy.restaurant.datahandler;

import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.Restaurant;

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
      * @return The current restaurant
      */
     Restaurant getRestaurant(final String phoneNumber, final String password);

     /**
      * <p>
      * Gets the restaurant if the id matches.
      * </p>
      *
      * @param restaurantId Represents the id of the current {@link Restaurant}
      * @return The restaurant object
      */
     Restaurant getRestaurantById(final long restaurantId);

     /**
      * <p>
      * Gets all the restaurants
      * </p>
      *
      * @return The list of all restaurants
      */
     List<Restaurant> getRestaurants();

     /**
      * <p>
      * Loads all the restaurants data.
      * </p>
      *
      * @param restaurants Represents list of restaurants
      */
     boolean loadRestaurantsData(final List<Restaurant> restaurants);

     /**
      * <p>
      * Loads the food details given from restaurant.
      * </p>
      *
      * @param food Represents the current food added by the restaurant
      * @param restaurantId Represents the id of the Restaurant
      * @return True if food is added, false otherwise
      */
     boolean addFood(final Food food, final long restaurantId);

     /**
      * <p>
      * Loads the menucard of the restaurant.
      * </p>
      *
      * @param menuCard Contains the list of foods from the restaurant
      */
     void loadMenuCardData(final Map<Food, Long> menuCard);

     /**
      * <p>
      * Gets the available food quantity in the restaurant .
      * </p>
      *
      * @param foodId Represents the id of the current {@link Food} selected by the user
      * @return Available quantity from the selected restaurant
      */
     int getQuantity(final long foodId);

     /**
      * <p>
      * Gets the menucard of the selected restaurant by the user.
      * </p>
      *
      * @param restaurantId Represents the id of the Restaurant
      * @param foodTypeId Represents the id of the food type.
      * @return The list of menucard having foods
      */
     List<Food> getMenuCard(final long restaurantId, final int foodTypeId);

     /**
      * <p>
      * Removes the food from the restaurant.
      * </p>
      *
      * @param foodId Represents the id of the current {@link Food} selected by the user
      * @return True if food is removed, false otherwise
      */
     boolean removeFood(final long foodId);

     /**
      * <p>
      * Updates the data of the current restaurant user.
      * </p>
      *
      * @param restaurantId Represents the id 0f the current {@link Restaurant}
      * @param restaurantData Represents the data of the current user to be updated
      * @param type Represents the type of data of the current user to be updated
      * @return True if data is updated, false otherwise
      */
     boolean updateRestaurantData(final long restaurantId, final String type, final String restaurantData);
}
