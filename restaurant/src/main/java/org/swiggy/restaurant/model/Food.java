package org.swiggy.restaurant.model;

/**
 * <p>
 * Represents food entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class Food {

    private long id;
    private String name;
    private float rate;
    private FoodType foodType;
    private int foodQuantity;

    public Food() {
    }

    public Food(final String foodName, final float rate, final FoodType type, final int foodQuantity) {
        this.name = foodName;
        this.rate = rate;
        this.foodType = type;
        this.foodQuantity = foodQuantity;
    }

    public void setFoodId(final int id) {
        this.id = id;
    }

    public long getFoodId() {
        return id;
    }

    public String getFoodName() {
        return name;
    }

    public void setFoodName(final String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setFoodRate(final float rate) {
        this.rate = rate;
    }

    public FoodType getType() {
        return foodType;
    }

    public void setFoodType(final FoodType foodType) {
        this.foodType = foodType;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(final int foodQuantity) {
        this.foodQuantity = foodQuantity;
    }
}