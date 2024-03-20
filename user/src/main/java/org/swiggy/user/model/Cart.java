package org.swiggy.user.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.swiggy.validator.validatorgroup.cart.ClearCartValidator;
import org.swiggy.validator.validatorgroup.cart.DeleteCartValidator;
import org.swiggy.validator.validatorgroup.cart.PostCartValidator;
import org.swiggy.validator.validatorgroup.cart.GetCartValidator;

import java.util.Objects;

/**
 * <p>
 * Represents cart entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class Cart {

    @Positive(message = "cart id can't be negative", groups = {DeleteCartValidator.class})
    private long id;
    @NotNull(message = "userId can't be null", groups = {PostCartValidator.class, GetCartValidator.class, ClearCartValidator.class})
    @Positive(message = "user id can't be negative", groups = {PostCartValidator.class, GetCartValidator.class, ClearCartValidator.class})
    private long userId;
    @NotNull(message = "foodId can't be null", groups = {PostCartValidator.class})
    @Positive(message = "food id can't be negative", groups = {PostCartValidator.class})
    private long foodId;
    @NotNull(message = "foodName can't be null", groups = {PostCartValidator.class})
    @Pattern(message = "enter a valid food name", regexp = "^[A-Za-z][A-Za-z\\s]{0,20}$", groups = {PostCartValidator.class})
    private String foodName;
    @NotNull(message = "restaurantId can't be null", groups = {PostCartValidator.class})
    @Positive(message = "restaurant id can't be negative", groups = {PostCartValidator.class})
    private long restaurantId;
    @NotNull(message = "restaurantName can't be null", groups = {PostCartValidator.class})
    @Pattern(message = "enter a valid restaurant name", regexp = "^[A-Za-z][A-Za-z\\s]{0,20}$", groups = {PostCartValidator.class})
    private String restaurantName;
    @NotNull(message = "quantity can't be null", groups = {PostCartValidator.class})
    @Positive(message = "quantity can't be negative", groups = {PostCartValidator.class})
    private int quantity;
    @NotNull(message = "amount can't be null", groups = {PostCartValidator.class})
    @Positive(message = "amount can't be negative", groups = {PostCartValidator.class})
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

    @Override
    public boolean equals(final Object object) {
        return ! Objects.isNull(object) && getClass() == object.getClass() && this.hashCode() == object.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
