package com.anonymous.mealmate.model.repository;


import android.util.Log;


import com.anonymous.mealmate.model.dao.FoodDao;
import com.anonymous.mealmate.model.dao.MealDao;
import com.anonymous.mealmate.model.dao.MealFoodDao;
import com.anonymous.mealmate.model.database.AppDatabase;

import com.anonymous.mealmate.model.dto.FoodDto;
import com.anonymous.mealmate.model.dto.MealDto;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.model.entity.MealFood;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class MealFoodRepository {
    private final AppDatabase appDatabase;
    private final MealFoodDao mealFoodDao;
    @Inject
    public MealFoodRepository(AppDatabase appDatabase) {
        this.mealFoodDao = appDatabase.mealFoodDao();
        this.appDatabase = appDatabase;
    }
    public Long insertMealFood(MealFood mealFood){
        return mealFoodDao.insertMealFood(mealFood);
    }
    public int updateMealFood(MealFood mealFood){
        return mealFoodDao.updateMealFood(mealFood);
    }
    public int deleteMealFood(MealFood mealFood){
        return mealFoodDao.deleteMealFood(mealFood);
    }
    public int deleteMealFoodByMealIndex(Long mealIndex){
        return mealFoodDao.deleteMealFoodByMealIndex(mealIndex);
    }

    public List<MealFood> getMealFoodsByMealIndex(long mealIndex) {
        return mealFoodDao.getMealFoodsByMealIndex(mealIndex);
    }

//    public void insertMealAndFoods(Meal meal, List<Food> foods) {
//        executorService.execute(() -> {
//            appDatabase.runInTransaction(() -> {
//                long mealId = mealDao.insertMeal(meal);// Meal 객체의 mealIndex는 DB에 삽입될 때 자동으로 설정됩니다.
//                long foodId;
//                Log.d("MealFoodRepository", "Meal " + " inserted with ID: " + mealId);
//                for (Food food : foods) {
//                    Food existingFood = foodDao.getFoodByNameSync(food.getFoodName());
//                    if (existingFood == null) {
//                        foodId = foodDao.insertFood(food); // Food 객체의 foodIndex도 DB에 삽입될 때 자동으로 설정됩니다.
//                        Log.d("MealFoodRepository", "Food " + food.getFoodName() + " inserted with ID: " + foodId);
//                    } else {
//                        food = existingFood;
//                        foodId = existingFood.getFoodIndex();
//                    }
//                    MealFood mealFood = new MealFood(mealId, foodId,1);
//                    long mealFoodId = mealFoodDao.insertMealFood(mealFood);
//                    Log.d("MealFoodRepository", "MealFood inserted with ID: " + mealFoodId);
//                }
//            });
//        });
//    }
//    public void insertMealDtoData(MealDto mealDto){
//        executorService.execute(() -> {
//            appDatabase.runInTransaction(() -> {
//                Meal meal = mealDto.extractMealData();
//                long mealId = mealDao.insertMeal(meal);
//                Log.e("MealFoodRepository", "Meal " + " inserted with ID: " + mealId+ " Date: "+meal.getMealDate());
//
//                long foodId;
//
//                for(FoodDto foodDto : mealDto.getFoodDtoList()){
//                    Food food = foodDto.extractFoodData();
//
//                    Food existingFood = foodDao.getFoodByNameSync(food.getFoodName());
//
//                    if (existingFood == null) {
//                        foodId = foodDao.insertFood(food); // Food 객체의 foodIndex도 DB에 삽입될 때 자동으로 설정됩니다.
//                        Log.d("MealFoodRepository", "Food " + food.getFoodName() + " inserted with ID: " + foodId);
//                    } else {
//                        food = existingFood;
//                        foodId = existingFood.getFoodIndex();
//                    }
//                    long mealFoodId = mealFoodDao.insertMealFood(new MealFood(mealId,foodId,foodDto.getMealFoodAmount()));
//
//                    Log.d("MealFoodRepository", "MealFood inserted with ID: " + mealFoodId);
//                }
//            });
//        });
//    }
}
