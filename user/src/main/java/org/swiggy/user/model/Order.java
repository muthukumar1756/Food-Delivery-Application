package org.swiggy.user.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.swiggy.validator.validatorgroup.cart.PostCartValidator;
import org.swiggy.validator.validatorgroup.order.GetOrderValidator;
import org.swiggy.validator.validatorgroup.order.PostOrderValdiator;

import java.util.Objects;

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
    @NotNull(message = "userId can't be null", groups = {PostOrderValdiator.class, GetOrderValidator.class})
    @Positive(message = "user id can't be negative", groups = {PostOrderValdiator.class, GetOrderValidator.class})
    private long userId;
    @NotNull(message = "cartId can't be null", groups = {PostOrderValdiator.class})
    @Positive(message = "cart id can't be negative", groups = {PostOrderValdiator.class})
    private long cartId;
    @NotNull(message = "foodId can't be null", groups = {PostOrderValdiator.class})
    @Positive(message = "restaurant id can't be negative", groups = {PostOrderValdiator.class})
    private long foodId;
    @NotNull(message = "foodName can't be null", groups = {PostOrderValdiator.class})
    @Pattern(message = "enter a valid food name", regexp = "^[A-Za-z][A-Za-z\\s]{0,20}$", groups = {PostCartValidator.class})
    private String foodName;
    @NotNull(message = "restaurantId can't be null", groups = {PostOrderValdiator.class})
    @Positive(message = "Restaurant Id Can't Be Negative", groups = {PostOrderValdiator.class})
    private long restaurantId;
    @NotNull(message = "restaurantName can't be null", groups = {PostOrderValdiator.class})
    @Pattern(message = "enter a valid restaurant name", regexp = "^[A-Za-z][A-Za-z\\s]{0,20}$", groups = {PostCartValidator.class})
    private String restaurantName;
    @NotNull(message = "quantity can't be null", groups = {PostOrderValdiator.class})
    @Positive(message = "quantity can't be negative", groups = {PostOrderValdiator.class})
    private int quantity;
    @NotNull(message = "amount can't be null", groups = {PostOrderValdiator.class})
    @Positive(message = "amount can't be negative", groups = {PostOrderValdiator.class})
    private float amount;
    @NotNull(message = "addressId can't be null", groups = {PostOrderValdiator.class})
    @Positive(message = "address id can't be negative", groups = {PostOrderValdiator.class})
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(final long userId) {
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

    @Override
    public boolean equals(final Object object) {
        return ! Objects.isNull(object) && getClass() == object.getClass() && this.hashCode() == object.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}