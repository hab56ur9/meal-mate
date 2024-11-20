package com.anonymous.mealmate.model.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.anonymous.mealmate.model.entity.JoinResult;
import com.anonymous.mealmate.model.entity.Weekly;

import java.util.List;

@Dao
public interface JoinDao {
    @Query("select mealFood.*,food.* from mealFood left join food on mf_foodIndex = foodIndex where mf_mealIndex = :mealIndex")
    List<JoinResult> getMealFoodWithFoodByMealIndex(Long mealIndex);

    /*@Query("SELECT meal.mealDate, COALESCE(SUM(food.foodKcal * CAST(mealFood.mealFoodAmount AS float)), 0) as totalCalories FROM meal JOIN mealFood ON meal.mealIndex = mealFood.mf_mealIndex JOIN food ON mealFood.mf_foodIndex = food.foodIndex WHERE meal.mealDate BETWEEN :startDate AND :endDate AND meal.checked != 0 GROUP BY meal.mealDate ORDER BY meal.mealDate;")
    List<Weekly> getWeeklyTotalCalories(String startDate, String endDate);*/

    @Query("SELECT meal.mealDate As date, COALESCE(SUM(food.foodKcal * CAST(mealFood.mealFoodAmount AS float)), 0) as totalCalories FROM meal JOIN mealFood ON meal.mealIndex = mealFood.mf_mealIndex JOIN food ON mealFood.mf_foodIndex = food.foodIndex WHERE meal.mealDate BETWEEN :startDate AND :endDate AND meal.mealDate IS NOT NULL AND meal.checked != 0 GROUP BY meal.mealDate ORDER BY meal.mealDate;")
    List<Weekly> getWeeklyTotalCalories(String startDate, String endDate);

    @Query("SELECT meal.mealDate FROM meal WHERE meal.mealDate IS NOT NULL")
    List<String> getMealDateList();

    /*@Query("SELECT meal.mealDate, SUM(food.foodKcal * mealFood.mealFoodAmount) as totalCalories FROM meal JOIN mealFood ON meal.mealIndex = mealFood.mf_mealIndex JOIN food ON mealFood.mf_foodIndex = food.foodIndex WHERE meal.mealDate >= strftime('%y%m%d', 'now') AND meal.mealDate < strftime('%y%m%d', 'now', '-7 day') GROUP BY meal.mealDate ORDER BY meal.mealDate;")
    List<Weekly> getWeeklyTotalCalories();*/
}
