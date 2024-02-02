package org.swiggy.model;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Represents cart entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class Cart {

    private final Map<Food, Integer> cart;

    public Cart() {
        this.cart = new HashMap<>();
    }

    public void addFood(final Food food, final int quantity) {
        if (null ==  cart.get(food)){
            cart.put(food, quantity);
        } else {
            final int currentQuantity = cart.get(food);

            cart.put(food, currentQuantity + quantity);
        }
    }

    public Map<Food, Integer> getCart() {
        return cart;
    }
}
