package org.swiggy.model;

/**
 * <p>
 * Represents food entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class Food {

    private final String name;
    private final float rate;
    private final FoodType foodType;

    private int id;
    private int foodQuantity;

    public Food(final String foodName, final float rate, final FoodType type, final int foodQuantity) {
        this.name = foodName;
        this.rate = rate;
        this.foodType = type;
        this.foodQuantity = foodQuantity;
    }

    public void setFoodId(final int id) {
        this.id = id;
    }

    public void updateQuantity(int foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public int getFoodId() {
        return id;
    }

    public String getFoodName() {
        return name;
    }

    public float getRate() {
        return rate;
    }

    public FoodType getType() {
        return foodType;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }
}