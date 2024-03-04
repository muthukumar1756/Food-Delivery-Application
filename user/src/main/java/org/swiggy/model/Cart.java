package org.swiggy.model;

/**
 * <p>
 * Represents cart entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class Cart {

    private long id;
    private long userId;
    private long restaurantId;
    private String restaurantName;
    private long foodId;
    private String foodName;
    private int quantity;
    private float amount;
    private CartStatus cartStatus;

    public Cart() {
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(final long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(final long foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(final float amount) {
        this.amount = amount;
    }

    public CartStatus getCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(final CartStatus cartStatus) {
        this.cartStatus = cartStatus;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(final String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(final String foodName) {
        this.foodName = foodName;
    }
}
