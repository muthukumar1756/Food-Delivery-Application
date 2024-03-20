package org.swiggy.restaurant.model;

import jakarta.validation.constraints.Positive;
import org.swiggy.validator.validatorgroup.food.GetFoodValidator;

/**
 * <p>
 * Defines the menu card type.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public enum MenuCardType {
    VEG(1),
    NONVEG(2),
    BOTH(3);

    @Positive(message = "menu card type id can't be negative", groups = {GetFoodValidator.class})
    private final int id;

    MenuCardType(final int id) {
        this.id = id;
    }

    public static int getId(final MenuCardType type) {
        return type.id;
    }

    public static MenuCardType getTypeById(final int id) {
        for (MenuCardType type : values()) {

            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}
