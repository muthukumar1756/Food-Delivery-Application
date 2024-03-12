package org.swiggy.user.model;

/**
 * <p>
 * Represents order entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class Order {

    private long id;
    private long user_id;
    private long cartId;
    private long foodId;
    private String foodName;
    private long restaurantId;
    private String restaurantName;
    private int quantity;
    private float amount;
    private long addressId;

    public Order() {
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setRestaurantName(final String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setFoodName(final String foodName) {
        this.foodName = foodName;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public void setAmount(final float amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getAmount() {
        return amount;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(final long cartId) {
        this.cartId = cartId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(final long addressId) {
        this.addressId = addressId;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(final long user_id) {
        this.user_id = user_id;
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
}