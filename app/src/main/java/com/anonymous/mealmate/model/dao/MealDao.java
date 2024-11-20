package com.anonymous.mealmate.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.anonymous.mealmate.model.entity.JoinResult;
import com.anonymous.mealmate.model.entity.Meal;

import java.util.List;

@Dao
public interface MealDao {
    @Insert
    long insertMeal(Meal meal);

    @Update
    int updateMeal(Meal meal);

    @Delete
    int deleteMeal(Meal meal);

    // 모든 Meal을 가져옵니다.
    @Query("SELECT * FROM meal")
    LiveData<List<Meal>> getAllMeals();

    // 특정 날짜의 Meal을 가져옵니다.
    // 날짜 매개변수로 주면 그 날짜에 식단 정보를 모두 list로 return 해줌. 날짜 포맷은 "YYMMDD", "230528" 이런식으로
    @Query("SELECT * FROM meal WHERE mealDate = :mealDate")
    List<Meal> getMealsByDate(String mealDate);

    // 체크가 된 Meal을 가져옵니다.
    @Query("SELECT * FROM meal WHERE checked = 1")
    LiveData<List<Meal>> getCheckedMeals();

    @Query("select * from meal where mealDate = :mealDate order by mealCnt asc")
    List<Meal> getMealListByMealDate(String mealDate);

    @Query("select * from meal where mealPreset = :mealPresetName")
    List<Meal> getMealListByMealPreset(String mealPresetName);

    @Query("select distinct mealPreset from meal where mealPreset is not null")
    List<String> getMealPresetNameList();
}