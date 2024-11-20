package com.anonymous.mealmate.feature;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.viewmodel.FoodViewModel;
import com.anonymous.mealmate.viewmodel.MealCheckViewModel;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp //애플리케이션이 Hilt를 사용하도록 지정
public class MealMateApplication extends Application {
    private static final String TAG = "밀메이트 시작~!";
    @Override
    public void onCreate() {    //애플리케이션의 시작점
        super.onCreate();
        Log.d(TAG, "onCreate: 밀메이트 시작~!");
    }

    public void onTerminate() {     //애플리케이션의 종료점
        super.onTerminate();
        Log.d(TAG, "onTerminate: 밀메이트 종료~!");
    }

}

