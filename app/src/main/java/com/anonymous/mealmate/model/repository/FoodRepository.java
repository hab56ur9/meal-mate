package com.anonymous.mealmate.model.repository;

import androidx.lifecycle.LiveData;

import com.anonymous.mealmate.model.dao.FoodDao;
import com.anonymous.mealmate.model.database.AppDatabase;
import com.anonymous.mealmate.model.entity.Food;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FoodRepository {
    private final FoodDao foodDao;
    @Inject
    public FoodRepository(AppDatabase appDatabase) {
        this.foodDao = appDatabase.foodDao();
    }

    // ExecutorService는 Java의 동시성 패키지 중 하나로, 작업을 백그라운드 스레드에서 실행하는 방법을 추상화하는 역할
    // ExecutorService는 요청받은 작업을 스레드 풀에서 실행하며, 스레드 풀의 크기는 작업의 수에 따라 동적으로 변경될 수 있다. -> 리소스 사용 효율적 관리
    public Long insertFood(Food food) {
        Food existingFood = foodDao.getFoodByNameSync(food.getFoodName());
        if (existingFood == null) {
            return foodDao.insertFood(food);
        }
       return existingFood.getFoodIndex();
    }

    public Food getFoodByIndex(int index) {
        return foodDao.getFoodByIndex(index);
    }
    public int updateFood(Food food) {
        return foodDao.updateFood(food);
    }

    public int deleteFood(Food food) {
        return foodDao.deleteFood(food);
    }

    // LiveData를 이용하여 데이터를 가져옵니다.
    public List<Food> getEntireFoodList() {
        return foodDao.getEntireFoodList();
    }

    // 특정 음식의 좋아요 여부를 가져옵니다.
    public List<Food> getLikeFoodList() {
        return foodDao.getLikeFoodList();
    }

    // foodName과 일치하는 음식리스트 가져옴.
//    public LiveData<List<Food>> getFoodByExactName(String foodName) {
//        return foodDao.getFoodByName(foodName);
//    }

    // 비동기적으로 FoodName과 일치하는 음식을 가져옴. insert 전 검사하려고.
    public Food getFoodByNameSync(String foodName) {
        return foodDao.getFoodByNameSync(foodName);
    }

}
