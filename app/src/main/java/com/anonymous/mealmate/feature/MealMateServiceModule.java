package com.anonymous.mealmate.feature;

import com.anonymous.mealmate.service.MealMateService;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class MealMateServiceModule {
    @Provides
    @Singleton
    public MealMateService provideMealMateService(){
        return new MealMateService();
    }
}
