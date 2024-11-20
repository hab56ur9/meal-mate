package com.anonymous.mealmate.model.dto;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.model.entity.MealFood;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MealDto {
    @Nullable
    private Long mealIndex;
    private String mealDate;
    private Integer mealCnt;
    private Integer checked;
    private Float mealKcal;
    private Float mealGram;

    private String mealPresetName;
    private List<FoodDto> foodDtoList = new ArrayList<>();


    public MealDto(@Nullable Long mealIndex, String mealDate, Integer mealCnt, Integer checked, Float mealKcal, Float mealGram,@Nullable List<FoodDto> foodDtoList) {
        this.mealIndex = mealIndex;
        this.mealDate = mealDate;
        this.mealCnt = mealCnt;
        this.checked = checked;
        if (mealKcal != null)
            this.mealKcal = mealKcal;
        if(mealGram != null)
            this.mealGram = mealGram;
        if (foodDtoList != null)
            this.foodDtoList = foodDtoList;
    }

    // getter , setter

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

    public Float getMealKcal() {
        return mealKcal;
    }
    public Float getMealGram() {
        return mealGram;
    }
    public List<FoodDto> getFoodDtoList() {
        return foodDtoList;
    }


    public void setMealIndex(@Nullable Long mealIndex) {
        this.mealIndex = mealIndex;
    }

    public void setMealDate(String mealDate) {
        this.mealDate = mealDate;
    }

    public void setMealCnt(Integer mealCnt) {
        this.mealCnt = mealCnt;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public void setMealKcal(Float mealKcal) {
        this.mealKcal = mealKcal;
    }

    public void setMealGram(Float mealGram) {
        this.mealGram = mealGram;
    }

    public void setFoodDtoList(List<FoodDto> foodDtoList) {
        this.foodDtoList = foodDtoList;
    }

    //프리셋용 getter setter
    public String getMealPresetName() {
        return mealPresetName;
    }

    public void setMealPresetName(String mealPresetName) {
        this.mealPresetName = mealPresetName;
    }
}
