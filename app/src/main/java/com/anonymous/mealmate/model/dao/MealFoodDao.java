package com.anonymous.mealmate.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.anonymous.mealmate.model.entity.MealFood;

import java.util.List;


@Dao
public interface MealFoodDao {
    @Insert
    long insertMealFood(MealFood mealFood);

    @Update
    int updateMealFood(MealFood mealFood);

    @Delete
    int deleteMealFood(MealFood mealFood);

    // mealIndex로 MealFood를 가져옵니다.
    @Query("Select * from mealFood Where mf_mealIndex = :mealIndex")
    List<MealFood> getMealFoodsByMealIndex(Long mealIndex);

    @Query("delete from mealFood where mf_mealIndex = :mealIndex")
    int deleteMealFoodByMealIndex(Long mealIndex);

//    @Query("SELECT * FROM mealFood LEFT JOIN food ON mealFood.foodIndex = food.foodIndex")
//    List<MealFoodWithFood>

}
