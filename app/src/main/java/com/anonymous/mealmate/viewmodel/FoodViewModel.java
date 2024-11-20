package com.anonymous.mealmate.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anonymous.mealmate.api.FoodApiHelper;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.model.dto.FoodDto;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.model.repository.FoodApiRepository;
import com.anonymous.mealmate.model.repository.FoodRepository;
import com.anonymous.mealmate.service.ConversionService;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;

/**
 * class info :
 *  1. ViewModel class that extended AndroidViewModel
 *  2. MVVM 패턴 적용 , data binding 사용, observer 를 위한 liveData
 *  3. xml 에 onclick 으로 method binding , activity 에 data Stream 전달 과정 없이 ViewModel 로 data 전달.
 *  -> activity 는 data 와 완전히 분리 되어 자유 로운 View state control 이 가능
 *
 *  NOTE !! :
 *   MVVM ViewModel application  Control Flow example
 *    xml onclick -> ViewModel method execute -> data update ( with Model ) -> ViewModel liveData updated
 *    -> activity or fragment observer active -> update xml data
 *
 *  constructor :
 *   1. parameter 로 application 받아 super(application) -> AndroidViewModel
 *   2. connect with Repository coded Singleton class
 *   3. get LiveData from Repository connected Database and hold data as LiveData to observe at activity
 *
 *   connected XML list
 *   1. adapter_food.xml
 *   2. fragment_food.xml
 *   3. adapter_mealsubitem.xml
 */

@HiltViewModel
public class FoodViewModel extends ViewModel {

    private final FoodRepository foodRepository;
    private final FoodApiRepository foodApiRepository;
    private final ControlViewState controlViewState;
    private final ConversionService conversionService;

    private LiveData<List<FoodDto>> foodDtoListLiveData;
    private LiveData<List<FoodDto>> foodDtoListLivedataWithLikeState;
    public LiveData<List<FoodDto>> getFoodDtoListLiveData() {
        return foodDtoListLiveData;
    }

    public LiveData<List<FoodDto>> getFoodDtoListLivedataWithLikeState() {
        return foodDtoListLivedataWithLikeState;
    }

    private static FoodDto selectedFood = null; // 선택한 음식 정보 저장
    public static FoodDto getSelectedFood() {
        return selectedFood;
    }

    @Inject
    public FoodViewModel(ConversionService conversionService,FoodRepository foodRepository, FoodApiRepository foodApiRepository, ControlViewState controlViewState) {
        this.conversionService = conversionService;
        this.foodRepository = foodRepository;
        this.foodApiRepository = foodApiRepository;
        this.controlViewState = controlViewState;
        this.foodDtoListLiveData = conversionService.getFoodDtoListLiveData();
        this.foodDtoListLivedataWithLikeState = conversionService.getFoodDtoListLiveDataWithLikeState();
    }
    public void onSearchFood( String inputFoodName) {
        //TODO 이후 조건 받아 foodapi에서 받게하기
       //List<Food> searchResults = foodApiRepository.searchFood(inputFoodName);
//        conversionService.setBackgroundTask( () -> {
            conversionService.loadFoodDtoListByNameOfApi(inputFoodName);
//        }).run();
    }
    public void onLoadFoodData(){
        conversionService.setBackgroundTask( () -> {
            conversionService
                    .loadEntireFoodDtoList()
                    .loadLikeFoodDtoList();
        }).runInTransaction();
    }
    public void onStartFoodInfoDialog(FoodDto selectedFood) {
        this. selectedFood = selectedFood;
        controlViewState.activeDialogSignal(ControlViewState.DIALOG_FOOD_DATASET);
    } // 선택된 음식을 설정하는 메소드 추가
    public void onLikeStateChange(FoodDto foodDto) {
        foodDto.setFoodLike(foodDto.getFoodLike() == Food.FOOD_NOT_LIKE ? Food.FOOD_LIKE : Food.FOOD_NOT_LIKE);
        Log.e(getClass().getName(), "onLikeStateChange: test" );
        conversionService.setBackgroundTask(() -> {
            conversionService
                    .update(foodDto)
                    .loadEntireFoodDtoList()
                    .loadLikeFoodDtoList();
        }).runInTransaction();
    }

}

//xml binding method, do not erase
//binding test complete, enable to use

// Food Adapter에 하트 버튼을 눌렀을때 Food db를 업데이트 해주는 메서드

/*  connected fragment_food.xml
 *  id: btn_food_search
 *  input Food name to search
 *
 *  get data from FoodRepository.class */

//    public void onLoadEntireFoodData(){
//
//        conversionService.setBackgroundTask( () -> {
//            conversionService.loadEntireFoodDtoList();
//        }).runInTransaction();
//    }
//    public void onLoadLikeStateFoodData(){
//        conversionService.setBackgroundTask( () -> {
//            conversionService.loadLikeFoodDtoList();
//        }).runInTransaction();
//    }

// searchResults = foodApiRepository.getSearchedFoodList();
//allFoods = foodRepository.getEntireFoodList();    // 모든 음식 정보를 가져옴.
// 검색결과와 에러메시지를 가져옴.
//    public LiveData<List<Food>> getSearchedFoodList() {
//        return searchResults;
//    }

//    public LiveData<String> getErrorMessage() {
//        return errorMessage;
//    }

//    public void insert(Food food) { foodRepository.insertFood(food); }
//
//    public void update(Food food) {
//        foodRepository.updateFood(food);
//    }
//
//    public void delete(Food food) {
//        foodRepository.deleteFood(food);
//    }

// 특정 음식의 좋아요 여부를 가져옵니다.
//    public LiveData<List<Food>> getLikedFoods() {
//        conversionService.setBackgroundTask(() -> {
//
//        });
//        return foodRepository.getUserLikedFoods();
//    }
// 사용자가 특정 음식 이름을 입력하면 그 음식의 영양 정보를 반환함.
//    public Call<FoodApiHelper.ResponseClass> getFoodNtrItdntList(String foodName) {
//        return foodApiRepository.getFoodNtrItdntList(foodName);
//    }

//    public LiveData<List<Food>> getAllFoods() {
//        return allFoods;
//    }
//    private final MutableLiveData<Food> foodLiveData = new MutableLiveData<>(); // 특정 음식의 좋아요 여부를 나타내는 LiveData 추가
//   private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
//private final LiveData<List<Food>> allFoods;
//private final LiveData<List<Food>> searchResults;   // 검색 결과를 나타내는 LiveData 추가