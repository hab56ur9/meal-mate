package com.anonymous.mealmate.model.dto;

import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.model.entity.MealFood;

import javax.annotation.Nullable;

public class FoodDto {
    //meal data
    @Nullable
    private Long mealIndex;
    private String mealDate;
    private Integer mealCnt;
    private Integer checked;

    //mealFood Data
    // DB 업로드 시에 meal food table 은 의미가 없기에 업로드시에는 meal food table 생성 x , 초기 값을 주어 해결한다.
    private Integer mealFoodAmount = 1;

    //food data
    @Nullable
    private Long foodIndex;
    private String foodName;
    private Float food1serving;
    private Float foodKcal;
    private Float foodCarbohydrates;
    private Float foodProtein;
    private Float foodFat;
    private String food_company;
    private Integer foodLike;

    public FoodDto(@Nullable Long mealIndex,
                   String mealDate,
                   Integer mealCnt,
                   Integer checked,
                   Integer mealFoodAmount,
                   @Nullable Long foodIndex,
                   String foodName,
                   Float food1serving,
                   Float foodKcal,
                   Float foodCarbohydrates,
                   Float foodProtein,
                   Float foodFat,
                   String food_company,
                   Integer foodLike) {
        this.mealIndex = mealIndex;
        this.mealDate = mealDate;
        this.mealCnt = mealCnt;
        this.checked = checked;
        this.mealFoodAmount = mealFoodAmount;
        this.foodIndex = foodIndex;
        this.foodName = foodName;
        this.food1serving = food1serving;
        this.foodKcal = foodKcal;
        this.foodCarbohydrates = foodCarbohydrates;
        this.foodProtein = foodProtein;
        this.foodFat = foodFat;
        this.food_company = food_company;
        this.foodLike = foodLike;
    }

    // meal data 는 은닉 하여 MealDto 에서만 수정 하게 설정 only use getter
    public Long getMealIndex() {
        return mealIndex;
    }

    public String getMealDate() {
        return mealDate;
    }

    public Integer getMealCnt() {
        return mealCnt;
    }

    public Integer getChecked() {
        return checked;
    }

    public FoodDto setMealIndex(@Nullable Long mealIndex) {
        this.mealIndex = mealIndex;
        return this;
    }

    public FoodDto setMealDate(String mealDate) {
        this.mealDate = mealDate;
        return this;
    }

    public FoodDto setMealCnt(Integer mealCnt) {
        this.mealCnt = mealCnt;
        return this;
    }

    public FoodDto setChecked(Integer checked) {
        this.checked = checked;
        return this;
    }


    public Integer getMealFoodAmount() {
        return mealFoodAmount;
    }

    public Long getFoodIndex() {
        return foodIndex;
    }

    public String getFoodName() {
        return foodName;
    }

    public Float getFood1serving() {
        return food1serving;
    }

    public Float getFoodKcal() {
        return foodKcal;
    }

    public Float getFoodCarbohydrates() {
        return foodCarbohydrates;
    }

    public Float getFoodProtein() {
        return foodProtein;
    }

    public Float getFoodFat() {
        return foodFat;
    }

    public String getFood_company() {
        return food_company;
    }

    public Integer getFoodLike() {
        return foodLike;
    }
    public FoodDto setFoodLike(Integer state){
        this.foodLike = state;
        return this;
    }

    public FoodDto addAmount() {
        mealFoodAmount++;
        return this;
    }

    public FoodDto minusAmount() {
        if (mealFoodAmount != 1)
            mealFoodAmount--;
        return this;
    }
}
