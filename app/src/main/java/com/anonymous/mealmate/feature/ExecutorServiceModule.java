package com.anonymous.mealmate.feature;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

// ExecutorService를 제공하는 모듈. ExecutorService는 비동기 작업을 수행하는데 사용.
@Module
@InstallIn(SingletonComponent.class)
public class ExecutorServiceModule {

    @Provides
    @Singleton
    public ExecutorService provideExecutorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
}
