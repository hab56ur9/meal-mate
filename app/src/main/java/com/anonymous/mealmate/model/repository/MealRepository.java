package com.anonymous.mealmate.model.repository;

import androidx.lifecycle.LiveData;
import com.anonymous.mealmate.model.dao.MealDao;
import com.anonymous.mealmate.model.database.AppDatabase;
import com.anonymous.mealmate.model.entity.Meal;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MealRepository {
    private final MealDao mealDao;
    private final AppDatabase appDatabase;

    @Inject
    public MealRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.mealDao = appDatabase.mealDao();
    }

    public Long insertMeal(Meal meal) {
        return mealDao.insertMeal(meal);
    }

    public int updateMeal(Meal meal) {
        return mealDao.updateMeal(meal);
    }

    public int deleteMeal(Meal meal) {
        return mealDao.deleteMeal(meal);
    }


    public List<Meal> getMealListByMealDate(String date) {
        return mealDao.getMealListByMealDate(date);
    }

    public List<Meal> getMealListByMealPreset(String mealPreset) {
        return mealDao.getMealListByMealPreset(mealPreset);
    }

    public List<String> getMealPresetNameList() {
        return mealDao.getMealPresetNameList();
    }

    public LiveData<List<Meal>> getCheckedMeals() {
        return mealDao.getCheckedMeals();
    }

    public LiveData<List<Meal>> getAllMeals() {
        return mealDao.getAllMeals();
    }

    public List<Meal> getMealsByDate(String mealDate) {
        return mealDao.getMealsByDate(mealDate);
    }

    //todo 두 메서드가 동시에 실행되어 업데이트 된값을 로드하지 못하는 동기 오류가 발생, 해결 방법 찾기

    //todo 비동기 순차 작업 처리 coroutine 함수?? 사용 해보기
}