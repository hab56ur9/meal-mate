package com.anonymous.mealmate.feature;

import android.app.Application;
import android.content.Context;

import com.anonymous.mealmate.api.FoodApiHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

// 애플리케이션에서 사용되는 공통적인 객체를 제공하는 모듈. Context와 API Helper를 제공.
@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    public static Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    public FoodApiHelper provideFoodApiHelper(@ApplicationContext Context context) {
        return new FoodApiHelper(context);
    }

    @Provides
    @Singleton
    public FoodApiHelper.ApiService provideApiService(FoodApiHelper foodApiHelper) {
        return foodApiHelper.getApiService();
    }
}