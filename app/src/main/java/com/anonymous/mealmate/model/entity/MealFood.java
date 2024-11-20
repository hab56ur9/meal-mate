package com.anonymous.mealmate.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import javax.annotation.Nullable;

@Entity(tableName = "mealFood",
        primaryKeys = {"mf_mealIndex", "mf_foodIndex"},
        foreignKeys = {
                @ForeignKey(entity = Meal.class, parentColumns = "mealIndex", childColumns = "mf_mealIndex"),
                @ForeignKey(entity = Food.class, parentColumns = "foodIndex", childColumns = "mf_foodIndex")
        }, indices = {@Index(value = {"mf_mealIndex", "mf_foodIndex"}), @Index("mf_foodIndex")})

        //todo 제약 조건 때문에 한 식단에 두끼 이상의 음식이 있으면 에러 발생, 이후 동일한 음식 추가시 , 양을 늘리거나 하는 구분 로직 or meal food 자체의 프라이머리 키 인덱스 설정

public class MealFood {

    @NonNull
    @ColumnInfo(name = "mf_mealIndex")
    private Long mealIndex;

    @NonNull
    @ColumnInfo(name = "mf_foodIndex")
    private Long foodIndex;

    @ColumnInfo(name = "mealFoodAmount")
    private Integer mealFoodAmount = BASIC_AMOUNT;

    @Ignore
    private static final int BASIC_AMOUNT = 1;

    public MealFood(@Nullable Long mealIndex, @Nullable Long foodIndex, int mealFoodAmount) {
        this.mealIndex = mealIndex;
        this.foodIndex = foodIndex;
        this.mealFoodAmount = mealFoodAmount;
    }


    // getter and setter methods...
    public Long getMealIndex() {
        return mealIndex;
    }

    public Long getFoodIndex() {
        return foodIndex;
    }

    public Integer getMealFoodAmount() {
        return mealFoodAmount;
    }

    public MealFood setMealFoodAmount(int mealFoodAmount) {
        this.mealFoodAmount = mealFoodAmount;
        return this;
    }
}