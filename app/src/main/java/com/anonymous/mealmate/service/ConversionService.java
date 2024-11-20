package com.anonymous.mealmate.service;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.anonymous.mealmate.model.database.AppDatabase;
import com.anonymous.mealmate.model.dto.FoodDto;
import com.anonymous.mealmate.model.dto.MealDto;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.model.entity.JoinResult;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.model.entity.MealFood;
import com.anonymous.mealmate.model.repository.FoodApiRepository;
import com.anonymous.mealmate.model.repository.FoodRepository;
import com.anonymous.mealmate.model.repository.JoinRepository;
import com.anonymous.mealmate.model.repository.MealFoodRepository;
import com.anonymous.mealmate.model.repository.MealRepository;
import com.anonymous.mealmate.model.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ConversionService {
    private final MealMateService mealMateService;
    private final ExecutorService executorService;
    private final AppDatabase appDatabase;
    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;
    private final FoodApiRepository foodApiRepository;
    private final UserRepository userRepository;
    private final MealFoodRepository mealFoodRepository;
    private final JoinRepository joinRepository;
    private BackgroundService backgroundService;
    private MutableLiveData<List<MealDto>> mealDtoListLiveData = new MutableLiveData<>();

    public MutableLiveData<List<MealDto>> getMealDtoListLiveData() {
        return mealDtoListLiveData;
    }

    private MutableLiveData<List<FoodDto>> foodDtoListLiveData = new MutableLiveData<>();

    public LiveData<List<FoodDto>> getFoodDtoListLiveData() {
        return foodDtoListLiveData;
    }

    private MutableLiveData<List<FoodDto>> foodDtoListLiveDataWithLikeState = new MutableLiveData<>();

    public MutableLiveData<List<FoodDto>> getFoodDtoListLiveDataWithLikeState() {
        return foodDtoListLiveDataWithLikeState;
    }

    @Inject
    public ConversionService(MealMateService mealMateService, ExecutorService executorService, AppDatabase appDatabase, MealRepository mealRepository, FoodRepository foodRepository, FoodApiRepository foodApiRepository, UserRepository userRepository, MealFoodRepository mealFoodRepository, JoinRepository joinRepository) {
        this.mealMateService = mealMateService;
        this.executorService = executorService;
        this.appDatabase = appDatabase;
        this.mealRepository = mealRepository;
        this.foodRepository = foodRepository;
        this.foodApiRepository = foodApiRepository;
        this.userRepository = userRepository;
        this.mealFoodRepository = mealFoodRepository;
        this.joinRepository = joinRepository;
    }

    public interface BackgroundService {
        void onStartService();
    }

    public ConversionService setBackgroundTask(BackgroundService backgroundService) {
        this.backgroundService = backgroundService;
        return this;
    }

    public void run() {
        if (backgroundService != null) {
            executorService.submit(() -> backgroundService.onStartService());
        } else
            Log.e(getClass().getName(), "run: failed, the service is null ");
    }

    public void runInTransaction() {
        if (backgroundService != null) {
            executorService.submit(() -> {
                appDatabase.runInTransaction(() -> {
                    backgroundService.onStartService();
                });
            });
        }
    }

    public ConversionService insertMealDtoData(MealDto mealDto) {
        Meal meal = mealMateService.of(mealDto).extractEntityMealForInsert();
        long mealId = mealRepository.insertMeal(meal);
        for (FoodDto foodDto : mealDto.getFoodDtoList()) {
            Food food = mealMateService.of(foodDto).extractEntityFood();
            Long foodId = foodRepository.insertFood(food);
            Long mealFoodId = mealFoodRepository.insertMealFood(new MealFood(mealId, foodId, foodDto.getMealFoodAmount()));
            Log.d(getClass().getName(), "insertMealDtoPreset: mealFood is inserted with id : " + mealFoodId);
        }
        return this;
    }

    public ConversionService insertMealDtoPreset(MealDto mealDto) {
        Meal meal = mealMateService.of(mealDto).extractEntityMealPreset();
        long mealId = mealRepository.insertMeal(meal);
        for (FoodDto foodDto : mealDto.getFoodDtoList()) {
            Food food = mealMateService.of(foodDto).extractEntityFood();
            Long foodId = foodRepository.insertFood(food);
            Long mealFoodId = mealFoodRepository.insertMealFood(new MealFood(mealId, foodId, foodDto.getMealFoodAmount()));
            Log.d(getClass().getName(), "insertMealDtoPreset: mealFood is inserted with id : " + mealFoodId);
        }
        return this;
    }

    public ConversionService loadMealDtoListByDate(String mealDate) {
        loadMealDtoProcess(mealRepository.getMealListByMealDate(mealDate));
        return this;
    }

    public ConversionService loadMealDtoListByPreset(String mealPreset) {
        loadMealDtoProcess(mealRepository.getMealListByMealPreset(mealPreset));
        return this;
    }

    public ConversionService deleteMealDataByPreset(String mealPresetName) {
        deleteMealDtoProcess(mealRepository.getMealListByMealPreset(mealPresetName));
        return this;
    }

    public ConversionService deleteMealDataByDate(String mealDate) {
        deleteMealDtoProcess(mealRepository.getMealListByMealDate(mealDate));
        return this;
    }


    private void insertMealDtoProcess(Meal meal) {

    }

    private void loadMealDtoProcess(List<Meal> mealList) {
        List<MealDto> packedList = new ArrayList<>();
        for (Meal meal : mealList) {
            List<JoinResult> joinResultList = joinRepository.getMealFoodWithFoodByMealIndex(meal.getMealIndex());
            MealDto mealDto = new MealMateService.MealDtoBuilder().set(meal).build();
            for (JoinResult joinResult : joinResultList) {
                mealMateService.of(mealDto).add(
                        new MealMateService.FoodDtoBuilder()
                                .set(meal)
                                .set(joinResult.getFood())
                                .set(joinResult.getMealFood())
                                .build()
                );
            }
            packedList.add(mealDto);
        }
        mealDtoListLiveData.postValue(packedList);
    }

    private void deleteMealDtoProcess(List<Meal> mealList) {
        for (Meal meal : mealList) {
            mealFoodRepository.deleteMealFoodByMealIndex(meal.getMealIndex());
            mealRepository.deleteMeal(meal);
        }
    }


    public ConversionService update(MealDto mealDto) {
        mealRepository.updateMeal(mealMateService.of(mealDto).extractEntityMeal());
        return this;
    }

    public ConversionService update(FoodDto foodDto) {
        Food food = mealMateService.of(foodDto).extractEntityFood();
        if (food.getFoodIndex() == null)
            foodRepository.insertFood(food);
        else
            foodRepository.updateFood(food);
        if (foodDto.getMealIndex() != null)
            mealFoodRepository.updateMealFood(mealMateService.of(foodDto).extractEntityMealFood());
        return this;
    }

    //getter
    public MealRepository getMealRepository() {
        return mealRepository;
    }

    public FoodRepository getFoodRepository() {
        return foodRepository;
    }

    public MealFoodRepository getMealFoodRepository() {
        return mealFoodRepository;
    }

    public JoinRepository getJoinRepository() {
        return joinRepository;
    }

    public FoodApiRepository getFoodApiRepository() {
        return foodApiRepository;
    }

    public ConversionService loadFoodDtoListByNameOfApi(String foodName) {
        foodApiRepository
                .connect(foodDtoListLiveData)
                .activeSearchProcess(foodName);
        return this;
    }

    public ConversionService loadEntireFoodDtoList() {
        Log.e(getClass().getName(), "loadEntireFoodDtoList: teset");
        loadFoodDtoList(foodRepository.getEntireFoodList(), foodDtoListLiveData);
        return this;
    }

    public ConversionService loadLikeFoodDtoList() {
        loadFoodDtoList(foodRepository.getLikeFoodList(), foodDtoListLiveDataWithLikeState);
        return this;
    }

    private void loadFoodDtoList(List<Food> foodList, MutableLiveData<List<FoodDto>> foodDtoListLiveData) {
        List<FoodDto> foodDtoList = new ArrayList<>();
        for (Food food : foodList) {
            foodDtoList.add(
                    new MealMateService.FoodDtoBuilder()
                            .set(food)
                            .build()
            );
        }
        foodDtoListLiveData.postValue(foodDtoList);
    }

    //현재 안쓰는 기능
    public ConversionService insertFoodDtoData(FoodDto foodDto) {
        return this;
    }
}
