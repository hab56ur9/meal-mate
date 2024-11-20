package com.anonymous.mealmate.model.dto;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.anonymous.mealmate.model.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDto {

    private Long id;
    private String name;
    private Integer gender;
    private Float height;
    private Float weight;
    private Integer activityRatio;
    private Integer purpose;
    private Float targetWeight;
    private Integer age;

    private boolean userDataInserted = false;

    public UserDto set(User user) {
        this.id = user.getUserIndex();
        this.name = user.getName();
        this.gender = user.getGender();
        this.height = user.getHeight();
        this.weight = user.getWeight();
        this.activityRatio = user.getActivityLevel();
        this.purpose = user.getPurpose();
        this.targetWeight = user.getTargetWeight();
        this.age = user.getAge();

        userDataInserted = true;
        return this;

    }
    private MutableLiveData<List<MealDto>> mealDtoListLiveData = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<MealDto>> getMealDtoListLiveData() {
        return mealDtoListLiveData;
    }
    public List<MealDto> getMealDtoList(){
        return mealDtoListLiveData.getValue();
    }

    public UserDto add(MealDto mealDto){
        List<MealDto> updatedList = new ArrayList<>(mealDtoListLiveData.getValue());
        updatedList.add(mealDto);
        mealDtoListLiveData.setValue(updatedList);
        return this;
    }
    public UserDto remove(MealDto mealDto){
        List<MealDto> updatedList = new ArrayList<>(mealDtoListLiveData.getValue());
        updatedList.remove(mealDto);
        mealDtoListLiveData.setValue(updatedList);
        return this;
    }


    public boolean isUserDataInserted() {
        return userDataInserted;
    }

    //getter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getGender() {
        return gender;
    }

    public Float getHeight() {
        return height;
    }

    public Float getWeight() {
        return weight;
    }

    public Integer getActivityRatio() {
        return activityRatio;
    }

    public Integer getPurpose() {
        return purpose;
    }

    public Float getTargetWeight() {
        return targetWeight;
    }

    public Integer getAge() {
        return age;
    }
}
