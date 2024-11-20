package com.anonymous.mealmate.model.repository;

import static android.content.ContentValues.TAG;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.anonymous.mealmate.feature.BenedictCalculator;
import com.anonymous.mealmate.model.dao.UserDao;
import com.anonymous.mealmate.model.database.AppDatabase;
import com.anonymous.mealmate.model.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {
    private final UserDao userDao;
    private BenedictCalculator benedictCalculator;
    private final ExecutorService executorService;

    @Inject
    public UserRepository(UserDao userDao, BenedictCalculator benedictCalculator, ExecutorService executorService) {
        this.userDao = userDao;
        this.executorService = executorService;
        this.benedictCalculator = benedictCalculator;
    }

    public void insertUser(User user) {
        user.setUserIndex(null);

        executorService.execute(() -> {
            float bmr = benedictCalculator.calculate(user);

            user.setBmr(bmr);

            Log.d("UserRepository", "insertUser: " + user.toString());

            userDao.insertUser(user);

            Log.e(TAG, "insertUser: " );

            //DB에 잘 저장됐는지 확인
            //Log.e("UserRepository", "insertUser: " + userDao.getUser().getValue().toString());
        });
    }

    // Entity User
    public void userUpdate(User user) {

    }

    public void updateUser(User user) {
        executorService.execute(() -> userDao.updateUser(user));
    }

    public void deleteUser(User user) {
        executorService.execute(() -> userDao.deleteUser(user));
    }

    public LiveData<User> getUserByName(String userName) {
        return userDao.getUserByName(userName);
    }

    public LiveData<User> getUser() {
        return userDao.getUser();
    }

    //todo 하나의 사용자 정보를 업데이트 될때 마다 insert, 가장 최근에 업데이트 된 user 정보를 받아오는 메소드 작성 필요
}