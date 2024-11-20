package com.anonymous.mealmate.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.anonymous.mealmate.model.entity.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Insert
    long insertFood(Food food);

    @Update
    int updateFood(Food food);

    @Delete
    int deleteFood(Food food);

    @Query("SELECT * FROM food")
    List<Food> getEntireFoodList();

    // 특정 음식의 좋아요 여부를 가져옵니다.
    @Query("SELECT * FROM food WHERE foodLike = 1")
    List<Food> getLikeFoodList();

    @Query("SELECT * FROM food WHERE foodName = :foodName")
    List<Food> getFoodByName(String foodName);

    @Query("SELECT * FROM food WHERE foodName = :foodName LIMIT 1")
    Food getFoodByNameSync(String foodName);

    // foodIndex로 음식을 가져옵니다.
    @Query("SELECT * FROM food WHERE foodIndex = :foodIndex")
    Food getFoodByIndex(int foodIndex);


}
