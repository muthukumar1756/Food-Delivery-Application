package org.swiggy.model;

import java.util.Map;

/**
 * <p>
 * Represents user entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class User {

    private int id;
    private String name;
    private String phoneNumber;
    private String password;
    private String emailId;
    private String address;
    private Cart cart;

    public User(final String name, final String phoneNumber, final String emailId, final String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.emailId = emailId;
        this.cart = new Cart();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public Map<Food, Integer> getCartItems() {
        return cart.getCart();
    }

    public Cart getCart() {
        return cart;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailId(final String emailId) {
        this.emailId = emailId;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void addFoodToCart(final Food selectedFood, final int quantity) {
        cart.addFood(selectedFood, quantity);
    }
}
