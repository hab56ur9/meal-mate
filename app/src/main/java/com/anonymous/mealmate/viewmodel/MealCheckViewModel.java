package com.anonymous.mealmate.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.ViewModel;


import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.feature.Date;
import com.anonymous.mealmate.model.dto.FoodDto;
import com.anonymous.mealmate.model.dto.MealDto;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.service.ConversionService;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MealCheckViewModel extends ViewModel {
    private final ConversionService conversionService;
    private final ControlViewState controlViewState;
    private final Date date;
    private String mealKcalForDay;
    private LiveData<List<MealDto>> mealDtoListLiveData;
    public LiveData<List<MealDto>> getMealDtoListLiveData() {
        return mealDtoListLiveData;
    }

    @Inject
    public MealCheckViewModel(ConversionService conversionService, ControlViewState controlViewState, Date date) {
        this.conversionService = conversionService;
        this.controlViewState = controlViewState;
        this.date = date;
        //레파지토리와 변수 연결
        mealDtoListLiveData = conversionService.getMealDtoListLiveData();
        mealDtoListLiveData.observeForever(mealDtoList -> {
            setMealKcalForDay();
        });
    }

    public void onLoadMeal() {
        conversionService.setBackgroundTask(() -> {
            conversionService.loadMealDtoListByDate(date.getDateToString());
        }).runInTransaction();
        Log.e(getClass().getName(), "onLoadMeal: called selected date is " + date.getDateToString());
    }

    //binding method
    public void onMealChecked(MealDto mealDto) {
        mealDto.setChecked(mealDto.getChecked() == Meal.CHECKED ? Meal.UNCHECKED : Meal.CHECKED);
        conversionService.setBackgroundTask(() -> {
            conversionService
                    .update(mealDto)
                    .loadMealDtoListByDate(date.getDateToString());
        }).runInTransaction();

    }
    public void onDateSelected(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        date.set(year, month, dayOfMonth, calendar.get(Calendar.DAY_OF_WEEK));
        onLoadMeal();
    }
    public void onDateSelected(MaterialCalendarView widget,CalendarDay day,boolean selected){
        date.set(day.getYear(),day.getMonth(),day.getDay(),day.getCalendar().get(Calendar.DAY_OF_WEEK));
        onLoadMeal();
    }
    public void onDateSelected(){

    }
    public String getMealDataToString(MealDto mealDto) {
        String str = "";
        for (FoodDto foodDto : mealDto.getFoodDtoList())
            str += foodDto.getFoodName() + " (" + foodDto.getMealFoodAmount() * foodDto.getFood1serving() + "g)" + "\n";
        return str.trim();
    }

    public String getMealKcalForDay() {
        return mealKcalForDay;
    }
    public void setMealKcalForDay() {
        float sum = 0f;
        float check = 0f;
        for (MealDto mealDto : mealDtoListLiveData.getValue()) {
            sum += mealDto.getMealKcal();
            check += mealDto.getMealKcal() * mealDto.getChecked();
        }
        if(sum==0)
            mealKcalForDay ="식단을 계획 해 주세요";
        else if (sum == check)
            mealKcalForDay = "식단을 완료 했습니다.";
        else
            mealKcalForDay= ""+(int)sum+"kcal 중 " + (int) check + "kcal 섭취";
    }
    public void onStartMealDialog(){
        //controlViewStat.activeDialogSignal();
    }

    public void onStartDialogPurpose(){
        controlViewState.activeDialogSignal(ControlViewState.DIALOG_PURPOSE_CONFIG);
    }
    public void onStartDialogWeight(){
        controlViewState.activeDialogSignal(ControlViewState.DIALOG_WEIGHT_CONFIG);
    }
}
