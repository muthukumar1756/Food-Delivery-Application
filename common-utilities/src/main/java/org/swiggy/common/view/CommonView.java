package org.swiggy.common.view;

public interface CommonView {

    /**
     * <p>
     * Gets the value given by the user.
     * </p>
     *
     * @return The Value give by the user
     */
     int getValue();

    /**
     * <p>
     * Gets the information given by the user.
     * </p>
     *
     * @return The information given by the user
     */
    String getInfo();

    /**
     * Validates the user input for the back option.
     *
     * @param back The back choice of the user
     * @return True if back condition is satisfied, false otherwise
     */
    boolean isBackButton(final String back);
}
