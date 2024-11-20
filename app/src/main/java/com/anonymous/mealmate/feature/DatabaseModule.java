package com.anonymous.mealmate.feature;

import android.content.Context;

import androidx.room.Room;

import com.anonymous.mealmate.model.dao.FoodDao;
import com.anonymous.mealmate.model.dao.JoinDao;
import com.anonymous.mealmate.model.dao.MealDao;
import com.anonymous.mealmate.model.dao.MealFoodDao;
import com.anonymous.mealmate.model.dao.UserDao;
import com.anonymous.mealmate.model.database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

// DB와 관련된 객체를 제공하는 모듈. RoomDB와 관련된 DAO들을 제공. repository는 이 모듈에서 제공하는 DAO를 사용하여 생성.
@Module
@InstallIn(SingletonComponent.class)    // SingletonComponent를 사용하면, 애플리케이션의 생명주기에 따라 관리 됨.(앱 전체에 하나만 존재)
public class DatabaseModule {
    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "DATABASE_NAME")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    public FoodDao provideFoodDao(AppDatabase appDatabase) {
        return appDatabase.foodDao();
    }

    @Provides
    @Singleton
    public MealDao provideMealDao(AppDatabase appDatabase) {
        return appDatabase.mealDao();
    }

    @Provides
    @Singleton
    public MealFoodDao provideMealFoodDao(AppDatabase appDatabase) {
        return appDatabase.mealFoodDao();
    }

    @Provides
    @Singleton
    public UserDao provideUserDao(AppDatabase appDatabase) {
        return appDatabase.userDao();
    }

    @Provides
    @Singleton
    public JoinDao provideJoinDao(AppDatabase appDatabase){
        return appDatabase.joinDao();
    }

}
