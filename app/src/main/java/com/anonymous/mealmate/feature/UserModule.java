package com.anonymous.mealmate.feature;

import com.anonymous.mealmate.model.dao.UserDao;
import com.anonymous.mealmate.model.repository.UserRepository;

import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class UserModule {

    @Provides
    @Singleton
    public static BenedictCalculator provideBenedictCalculator() {
        return new BenedictCalculator();
    }

    @Provides
    @Singleton
    public static UserRepository provideUserRepository(UserDao userDao, BenedictCalculator benedictCalculator, ExecutorService executorService) {
        return new UserRepository(userDao, benedictCalculator, executorService);
    }
}
