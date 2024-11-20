package com.anonymous.mealmate.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.anonymous.mealmate.model.entity.User;

@Dao
public interface UserDao {
    @Insert
    long insertUser(User user);

    @Update
    int updateUser(User user);

    @Delete
    int deleteUser(User user);

    @Query("SELECT * FROM user WHERE name = :userName")
    LiveData<User> getUserByName(String userName);

// userIndex 제일 큰 값의 유저 하나 가져오기
    @Query("SELECT * FROM user WHERE userIndex = (SELECT MAX(userIndex) FROM user)")
    LiveData<User> getUser();
}