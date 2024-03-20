package org.swiggy.user.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.swiggy.validator.validatorgroup.user.LoginUserValidator;
import org.swiggy.validator.validatorgroup.user.PostUserValidator;
import org.swiggy.validator.validatorgroup.user.GetUserValidator;
import org.swiggy.validator.validatorgroup.user.PutUserValidator;

import java.util.Objects;

/**
 * <p>
 * Represents user entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class User {

    @Positive(message = "user id can't be negative", groups = {PutUserValidator.class, GetUserValidator.class})
    private long id;
    @NotNull(message = "name can't be null", groups = {PostUserValidator.class})
    @Pattern(message = "enter a valid name", regexp = "^[A-Za-z][A-Za-z\\s]{0,20}$", groups = {PutUserValidator.class, PostUserValidator.class})
    private String name;
    @NotNull(message = "phoneNumber can't be null", groups = {PostUserValidator.class})
    @Pattern(message = "enter a valid phone number", regexp = "^(0/91)?[6789]\\d{9}$", groups = {PutUserValidator.class, PostUserValidator.class, LoginUserValidator.class})
    private String phoneNumber;
    @NotNull(message = "password can't be null", groups = {PostUserValidator.class})
    @Pattern(message = "enter a valid password", regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$", groups = {PutUserValidator.class, PostUserValidator.class, LoginUserValidator.class})
    private String password;
    @NotNull(message = "emailId can't be null", groups = {PostUserValidator.class})
    @Pattern(message = "enter a valid email id", regexp = "^[a-z][a-z\\d._]+@[a-z]{5,20}.[a-z]{2,3}$", groups = {PutUserValidator.class, PostUserValidator.class, LoginUserValidator.class})
    private String emailId;

    public User() {
    }

    public User(final String name, final String phoneNumber, final String emailId, final String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.emailId = emailId;
    }

    public long getId() {
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

    public void setId(final long id) {
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

    @Override
    public boolean equals(final Object object) {
        return !Objects.isNull(object) && getClass() == object.getClass() && this.hashCode() == object.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailId, phoneNumber);
    }
}
