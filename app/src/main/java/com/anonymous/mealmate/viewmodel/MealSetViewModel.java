package com.anonymous.mealmate.viewmodel;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.feature.Date;
import com.anonymous.mealmate.model.dto.FoodDto;
import com.anonymous.mealmate.model.dto.MealDto;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.model.entity.Meal;


import com.anonymous.mealmate.service.ConversionService;
import com.anonymous.mealmate.service.MealMateService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MealSetViewModel extends ViewModel {


    private final MealMateService mealMateService;
    private final ConversionService conversionService;
    private final ControlViewState controlViewState;
    private Date date;

    private static final int CURSOR_NONE = -1;
    private static int positionCursor = CURSOR_NONE;
    private static List<MealDto> mealDtoList = new ArrayList<>();
    public ArrayList<MealDto> getMealDtoList() {
        return new ArrayList<>(mealDtoList);
    }
    private MutableLiveData<List<MealDto>> mealDtoListLiveData;
    private static FoodDto selectedFoodDto = null;
    public FoodDto getSelectedFoodDto() {
        return selectedFoodDto;
    }

    public LiveData<List<MealDto>> getMealDtoListLiveData() {
        return mealDtoListLiveData;
    }
    private static List<String> presetNameList = new ArrayList<>();
    public List<String> getPresetNameList() {
        return presetNameList;
    }
    @Inject
    public MealSetViewModel(MealMateService mealMateService, ConversionService conversionService, ControlViewState controlViewState, Date date) {
        this.mealMateService = mealMateService;
        this.conversionService = conversionService;
        this.controlViewState = controlViewState;
        this.date = date;
        //live data sync viewModel < - > service
        mealDtoListLiveData = conversionService.getMealDtoListLiveData();
    }
    public void onDateSelected(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        date.set(year, month, dayOfMonth, calendar.get(Calendar.DAY_OF_WEEK));
        onLoadMeal();
    }

    public void onLoadMeal() {
        Log.e(getClass().getName(), "onLoadMeal: called selected date is " + date.getDateToString());
        conversionService.setBackgroundTask(() -> {
            conversionService.loadMealDtoListByDate(date.getDateToString());
            presetNameList = new ArrayList<>(conversionService.getMealRepository().getMealPresetNameList());
        }).runInTransaction();
    }
    public void syncMealData() {
        for (int i = 0; i < mealDtoList.size(); i++) {
            mealDtoList.get(i).setMealCnt(i);
            mealMateService.of(mealDtoList.get(i)).sync();
        }
        setMealDtoListLiveData();
    }
    public void onStartAddFoodCycle(int position) {
        positionCursor = position;
        controlViewState.activeIntentSignal(ControlViewState.INTENT_SETMEAL_TO_FOOD);
        Log.e(getClass().getName(), "onStartAddFoodCycle: positionCursor is : " + positionCursor);
    }
    public void onFinishAddFoodCycle(FoodDto foodDto) {
        controlViewState.activeIntentSignal(ControlViewState.ACTIVITY_FINISH_FOOD);
        if (positionCursor != CURSOR_NONE) {
            MealDto mealDto = mealDtoList.get(positionCursor);
            MealMateService.MealService service = mealMateService.of(mealDto);
            if (!service.isFoodInList(foodDto)) {
                service.add(foodDto);
                Log.d(getClass().getName(), "onFinishAddFoodCycle: the food added");
            } else {
                service.addAmount(foodDto);
                Log.d(getClass().getName(), "onFinishAddFoodCycle: the food is already exist in List , mealAmount will increase");
            }
        } else
            Log.e(getClass().getName(), "onFinishAddFoodCycle: Error occurred, Check the positionCursor, current value is : " + positionCursor);
        positionCursor = CURSOR_NONE;
        syncMealData();
    }
    public void onSaveMeal() {
        Log.d(getClass().getName(), "onSaveMeal: activated");
        syncMealData();
        controlViewState.activeIntentSignal(ControlViewState.ACTIVITY_FINISH_SETMEAL);
        conversionService.setBackgroundTask(() -> {
            conversionService.deleteMealDataByDate(date.getDateToString());
            for (MealDto mealDto : getMealDtoList()) {
                mealDto.setMealDate(date.getDateToString());
                mealMateService.of(mealDto).sync();
                conversionService.insertMealDtoData(mealDto);
            }
            // 라이브 데이터 값 초기화
            mealDtoList = new ArrayList<>();
        }).runInTransaction();
    }
    public void onAddMealTime() {
        mealDtoList.add(
                new MealMateService.MealDtoBuilder().set(
                        // 선택된 날짜 String, 현재 리스트의 크기 -> mealCnt , check 로 meal 생성
                        new Meal(date.getDateToString(), mealDtoList.size(), Meal.UNCHECKED, null)
                ).build()
        );
        syncMealData();
    }
    public void onDeleteMealTime(MealDto mealDto) {
        mealDtoList.remove(mealDto);
        syncMealData();
    }
    public void onActiveDialogSavePreset() {
        controlViewState.activeDialogSignal(ControlViewState.DIALOG_MEAL_SAVE_PRESET);
    }
    public void onActiveDialogLoadPreset() {
        controlViewState.activeDialogSignal(ControlViewState.DIALOG_MEAL_LOAD_PRESET);
    }
    public void onLoadPreset(String mealPresetName) {
        conversionService.setBackgroundTask(() -> {
            conversionService.loadMealDtoListByPreset(mealPresetName);
        }).runInTransaction();
    }
    public void onSavePreset(View view) {
        String presetName = ((EditText) view).getText().toString();
        syncMealData();
        conversionService.setBackgroundTask(() -> {
            conversionService.deleteMealDataByPreset(presetName);
            for (MealDto mealDto : getMealDtoList()) {
                mealDto.setMealPresetName(presetName);
                conversionService.insertMealDtoPreset(mealDto);
            }
            presetNameList = new ArrayList<>(conversionService.getMealRepository().getMealPresetNameList());
            // 라이브 데이터 값 초기화
            mealDtoList = new ArrayList<>();
        }).runInTransaction();
    }
//    public void onShowHiddenView(View view) {
//        view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
//    }

    private void setMealDtoListLiveData() {
        mealDtoListLiveData.setValue(new ArrayList<>(mealDtoList));
    }
    public void setMealDtoList(){
        mealDtoList = new ArrayList<>(mealDtoListLiveData.getValue());
    }
    public void showDetailView(FoodDto foodDto){
        controlViewState.activeDialogSignal(ControlViewState.DIALOG_MEALFOOD_DATASET);
        selectedFoodDto = foodDto;
    }
    //        mealDtoListLiveData.observeForever(mealDtoList -> {
//            this.mealDtoList = new ArrayList<>(mealDtoList);
//            activeMealDtoDataChangedSignal();
//        });
    //    private void setObserver(){
//        mealDtoListLiveData.observeForever(observer);
//    }
//    private Observer<List<MealDto>> observer = new Observer<List<MealDto>>() {
//        @Override
//        public void onChanged(List<MealDto> updatedList) {
//            mealDtoList = new ArrayList<>(updatedList);
//            activeMealDtoDataChangedSignal();
//            mealDtoListLiveData.removeObserver(observer);
//        }
//    };
}
