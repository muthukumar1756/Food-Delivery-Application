package org.swiggy.service;

import java.util.Map;

import org.swiggy.model.Food;
import org.swiggy.model.User;

/**
 * <p>
 * Provides the services for the user cart.
 * </p>
 *
 * @author Muthu kumar v
 * @version 1.0
 */
public interface CartService {

     /**
      * <p>
      * Adds the selected food to the user cart.
      * </p>
      *
      * @param food Represents the current {@link Food} selected by the user
      * @param user Represents the current {@link User}
      * @param quantity Represents the quantity of the {@link Food} given by the current user
      * @return True if the food is added to the user cart, false otherwise
      */
     boolean addFoodToCart(final Food food, final User user, final int quantity, final int restaurantId);

     /**
      * <p>
      * Gets the cart of the current user.
      * </p>
      *
      * @param user Represents the current {@link User}
      * @return The map having all the foods from the user cart
      */
     Map<Food, Integer> getCart(final User user);

     /**
      * <p>
      * Removes the selected food from the user cart.
      * </p>
      *
      * @param user Represents the current {@link User}
      * @param food Represents the current {@link Food} selected by the user
      * @return True if the food is removed,false otherwise
      */
     boolean removeFood(final User user, final Food food);

     /**
      * <p>
      * Remove all the foods from the user cart.
      * </p>
      *
      * @param user Represents the current {@link User}
      * @return The true if the cart is cleared, false otherwise
      */
     boolean clearCart(final User user);
}
