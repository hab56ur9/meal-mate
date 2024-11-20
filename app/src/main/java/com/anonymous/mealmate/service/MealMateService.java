package com.anonymous.mealmate.service;


import android.util.Log;

import com.anonymous.mealmate.model.dto.FoodDto;
import com.anonymous.mealmate.model.dto.MealDto;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.model.entity.MealFood;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class  MealMateService {
    public MealService of(MealDto mealDto) {
        return new MealService(mealDto);
    }
    public FoodService of(FoodDto foodDto) {
        return new FoodService(foodDto);
    }
    public class MealService {

        MealDto mealDto;
        List<FoodDto> foodDtoList;

        Integer position = -1;

        public MealService(MealDto mealDto) {
            this.mealDto = mealDto;
            if (mealDto.getFoodDtoList() == null) {
                foodDtoList = new ArrayList<>();
                mealDto.setFoodDtoList(foodDtoList);
            }
            else
                foodDtoList = mealDto.getFoodDtoList();
        }

        public Meal extractEntityMeal() {
            return new Meal(
                    mealDto.getMealIndex(),
                    mealDto.getMealDate(),
                    mealDto.getMealCnt(),
                    mealDto.getChecked()
            );
        }
        public Meal extractEntityMealForInsert(){
            return new Meal(
                    mealDto.getMealDate(),
                    mealDto.getMealCnt(),
                    Meal.UNCHECKED
            );
        }
        public Meal extractEntityMealPreset(){
            return new Meal(
                    mealDto.getMealCnt(),
                    mealDto.getChecked(),
                    mealDto.getMealPresetName()
            );
        }
        public Boolean isFoodInList(Food food) {
            for (FoodDto foodDto : foodDtoList)
                if (foodDto.getFoodName().equals(food.getFoodName()))
                    return true;
            return false;
        }

        public Boolean isFoodInList(FoodDto foodDto) {
            for (FoodDto currentFoodDto : foodDtoList)
                if (currentFoodDto.getFoodName().equals(foodDto.getFoodName()))
                    return true;
            return false;
        }
        public MealService add(FoodDto foodDto) {
            foodDtoList.add(foodDto);
            sync();
            return this;
        }

        public MealService add(Food food) {
            foodDtoList.add(
                    new FoodDtoBuilder().set(extractEntityMeal()).set(food).build()
            );
            sync();
            return this;
        }
        public MealService remove(FoodDto foodDto) {
            foodDtoList.remove(foodDto);
            sync();
            return this;
        }
        public MealService addAmount(Food food) {
            for (FoodDto foodDto : foodDtoList)
                if (foodDto.getFoodName().equals(food.getFoodName()))
                    foodDto.addAmount();
            sync();
            return this;
        }
        public MealService addAmount(FoodDto foodDto) {
            for (FoodDto currentFoodDto : foodDtoList)
                if (currentFoodDto.getFoodName().equals(foodDto.getFoodName()))
                    currentFoodDto.addAmount();
            sync();
            return this;
        }
        public MealService sync(){
            float sumKcal =0f;
            float sumGram =0f;
            for(FoodDto foodDto : foodDtoList){
                of(foodDto).sync(mealDto);
                sumKcal+=foodDto.getFoodKcal()*foodDto.getMealFoodAmount();
                sumGram+=foodDto.getFood1serving()*foodDto.getMealFoodAmount();
            }
            mealDto.setMealKcal(sumKcal);
            mealDto.setMealGram(sumGram);
            return this;
        }
    }
    public class FoodService  {

        private FoodDto foodDto;

        public FoodService(FoodDto foodDto) {
            this.foodDto = foodDto;
        }

        public Food extractEntityFood() {
            return new Food(
                    foodDto.getFoodIndex(),
                    foodDto.getFoodName(),
                    foodDto.getFood1serving(),
                    foodDto.getFoodKcal(),
                    foodDto.getFoodCarbohydrates(),
                    foodDto.getFoodProtein(),
                    foodDto.getFoodFat(),
                    foodDto.getFood_company(),
                    foodDto.getFoodLike()
            );
        }

        public MealFood extractEntityMealFood() {
            return new MealFood(foodDto.getMealIndex(),
                    foodDto.getFoodIndex(),
                    foodDto.getMealFoodAmount());
        }

        public FoodService addAmount() {
            foodDto.addAmount();
            return this;
        }

        public FoodService minusAmount() {
            foodDto.minusAmount();
            return this;
        }
        public FoodService sync(MealDto mealDto){
            foodDto.setMealIndex(mealDto.getMealIndex());
            foodDto.setMealDate(mealDto.getMealDate());
            foodDto.setMealCnt(mealDto.getMealCnt());
            foodDto.setChecked(mealDto.getChecked());
            return this;
        }
    }
    public static class MealDtoBuilder {
        @Nullable
        private Long mealIndex;
        private String mealDate;
        private Integer mealCnt;
        private Integer checked;
        private Float mealKcal =0f;
        private Float mealGram = 0f;
        private List<FoodDto> foodDtoList = new ArrayList<>();

        public MealDtoBuilder set(Meal meal) {
            this.mealIndex = meal.getMealIndex();
            this.mealDate = meal.getMealDate();
            this.mealCnt = meal.getMealCnt();
            this.checked = meal.getChecked();
            return this;
        }

        public MealDtoBuilder set(List<FoodDto> foodDtoList) {
            this.foodDtoList = foodDtoList;
            for(FoodDto foodDto : foodDtoList ){
                mealKcal +=foodDto.getFoodKcal();
                mealGram +=foodDto.getFood1serving()*foodDto.getMealFoodAmount();
            }
            return this;
        }

        public MealDto build() {
            return new MealDto(
                    mealIndex,
                    mealDate,
                    mealCnt,
                    checked,
                    mealKcal,
                    mealGram,
                    foodDtoList
            );
        }
    }
    public static class FoodDtoBuilder {
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

        public FoodDtoBuilder set(Meal meal) {
            this.mealIndex = meal.getMealIndex();
            this.mealDate = meal.getMealDate();
            this.mealCnt = meal.getMealCnt();
            return this;
        }

        public FoodDtoBuilder set(Food food) {
            this.foodIndex = food.getFoodIndex();
            this.foodName = food.getFoodName();
            this.foodKcal = food.getFoodKcal();
            this.foodCarbohydrates = food.getFoodCarbohydrates();
            this.food1serving = food.getFood1serving();
            this.foodProtein = food.getFoodProtein();
            this.foodFat = food.getFoodFat();
            this.food_company = food.getFood_company();
            this.foodLike = food.getFoodLike();
            return this;
        }

        public FoodDtoBuilder set(MealFood mealFood) {
            if (this.mealIndex == null)
                this.mealIndex = mealFood.getMealIndex();
            if (this.foodIndex == null)
                this.foodIndex = mealFood.getFoodIndex();

            this.mealFoodAmount = mealFood.getMealFoodAmount();
            return this;
        }

        public FoodDto build() {
            return new FoodDto(
                    mealIndex,
                    mealDate,
                    mealCnt,
                    checked,
                    mealFoodAmount,
                    foodIndex,
                    foodName,
                    food1serving,
                    foodKcal,
                    foodCarbohydrates,
                    foodProtein,
                    foodFat,
                    food_company,
                    foodLike
            );
        }
    }
}



