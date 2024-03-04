package org.swiggy.model;

/**
 * <p>
 * Represents restaurant entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class Restaurant {

    private String name;
    private String phoneNumber;
    private String password;
    private String emailId;
    private int id;

    public Restaurant() {
    }

    public Restaurant(final String name, final String phoneNumber, final String emailId, final String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.emailId = emailId;
    }

    public void setRestaurantId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getRestaurantId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(final String emailId) {
        this.emailId = emailId;
    }
}