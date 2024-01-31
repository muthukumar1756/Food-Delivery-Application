package org.swiggy.model;

/**
 * <p>
 * Provides the food type.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public enum FoodType {
    VEG(1),
    NONVEG(2);

    private final int id;

    FoodType(final int id) {
        this.id = id;
    }

    /**
     *
     * @param foodType Represents the category of the food
     * @return The id of the food category
     */
    public static int getId(FoodType foodType) {
        return foodType.id;
    }
}
