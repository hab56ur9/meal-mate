package com.anonymous.mealmate.model.repository;


import static com.anonymous.mealmate.constants.Constants.*;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anonymous.mealmate.api.FoodApiHelper;

import com.anonymous.mealmate.model.dto.FoodDto;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.service.MealMateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class FoodApiRepository {
    private static final String ERROR_MESSAGE = "Error: ";
    public static final int OFF = 0;
    public static final int ON = 1;

    public static int state = OFF;

    public static int getState() {
        return state;
    }

    public static void setState(int state) {
        FoodApiRepository.state = state;
    }
    public void close(){
        foodApiLiveData=null;
    }
    public FoodApiRepository connect(MutableLiveData<List<FoodDto>> foodDtoListLiveData){
        this.foodApiLiveData = foodDtoListLiveData;
        return this;
    }
    private FoodApiHelper foodApiHelper;
    private FoodApiHelper.ApiService apiService;
    //LiveData는 변경 불가능한 데이터, MutableLiveData 데이터 변경 가능.
    // API 호출 결과가 searchResults에 저장 -> MutableLiveData를 관찰하고 있는 UI 컴포넌트가 자동으로 업데이트

    private MutableLiveData<List<FoodDto>> foodApiLiveData = new MutableLiveData<>();

    public MutableLiveData<List<FoodDto>> getFoodApiLiveData() {
        return foodApiLiveData;
    }

    // FoodApiHelper 클래스의 인스턴스를 가져오고, apiService와 searchedFoodList 초기화.
    @Inject
    public FoodApiRepository(FoodApiHelper.ApiService apiService) {
        this.foodApiHelper = foodApiHelper;
        this.apiService = apiService;
    }

    // foodName으로 API 호출하여 검색 -> 결과는 Food 객체 리스트로 변환 -> searchResults에 저장
    public FoodApiRepository activeSearchProcess(String foodName) {
        Call<FoodApiHelper.ResponseClass> call = getFoodNtrItdntList(foodName);
        call.enqueue(response);
        return this;
    }

    Callback<FoodApiHelper.ResponseClass> response = new Callback<FoodApiHelper.ResponseClass>() {    // Callback 인터페이스를 구현한 익명 클래스 정의. 응답을 받았을 때 호출되는 메소드를 정의.
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        // onResponse 메소드: 응답을 성공적으로 받았을 때 호출되는 메소드
        public void onResponse(Call<FoodApiHelper.ResponseClass> call, Response<FoodApiHelper.ResponseClass> response) {
            Optional.ofNullable(response.body())
                    .map(FoodApiHelper.ResponseClass::getBody)
                    .map(FoodApiHelper.ResponseClass.Body::getItems)
                    .ifPresent(items -> {
                        foodApiLiveData.postValue(convertItemsToFood(items));
                        close();
                    });
        }

        @Override
        public void onFailure(Call<FoodApiHelper.ResponseClass> call, Throwable t) {
            Log.e(ERROR_MESSAGE, t.getMessage());
            handleApiError(t);
        }
    };

    // API 호출 메소드 정의.
    public Call<FoodApiHelper.ResponseClass> getFoodNtrItdntList(String foodName) {
        return apiService.getFoodNtrItdntList(SERVICE_KEY, foodName, null, null, null, null, RESPONSE_TYPE);
    }

    // API 호출 결과를 Food 객체 리스트로 변환.
    private List<FoodDto> convertItemsToFood(List<FoodApiHelper.ResponseClass.Body.Item> items) {
        List<FoodDto> foodDtoList = new ArrayList<>();
        for (FoodApiHelper.ResponseClass.Body.Item item : items) {
            foodDtoList.add(
                    new MealMateService.FoodDtoBuilder().set(
                            new Food(
                                    item.getFood_name(),
                                    item.getFood_1serving(),
                                    item.getFood_kcal(),
                                    item.getFood_carbohydrates(),
                                    item.getFood_protein(),
                                    item.getFood_fat(),
                                    item.getFood_company()
                            )
                    ).build()
            );
        }
        return foodDtoList;
    }

    // Implement a method to handle errors
    private void handleApiError(Throwable t) {
        // This can include showing a user-friendly error message, logging the error, etc.
    }
}
