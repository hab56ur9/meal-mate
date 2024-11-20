package com.anonymous.mealmate.model.entity;


import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class JoinResult {
    @Embedded //@Embedded 어노테이션을 사용하여 객체를 인식
    private Meal meal=null;
    @Embedded
    private Food food=null;
    @Embedded
    private MealFood mealFood = null;
    @Embedded
    private User user = null;

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public MealFood getMealFood() {
        return mealFood;
    }

    public void setMealFood(MealFood mealFood) {
        this.mealFood = mealFood;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
