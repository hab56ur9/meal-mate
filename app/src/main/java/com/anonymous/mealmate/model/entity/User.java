package com.anonymous.mealmate.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

@Entity(tableName = "user")
public class User {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long userIndex;

    public String name;
    public Float height;
    public Float weight;
    public Float targetWeight;
    public Integer age;
    public Integer gender;
    public Integer activityLevel;
    public Integer purpose;
    public Float bmr;

    @Ignore
    public static final int MALE = 1;
    public static final int FEMALE = 2;
    public static final int SEDENTARY = 1;
    public static final int LIGHT = 2;
    public static final int MODERATE = 3;
    public static final int ACTIVE = 4;
    public static final int VERYACTIVE = 5;
    public static final int DIET = 1;
    public static final int BULK = 2;
    public static final int MAINTAIN = 3;

    public User() {}

    public User(@NotNull String name, Float height, Float weight, Integer age, Integer gender, Integer activityLevel, Integer purpose, Float bmr){
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.activityLevel = activityLevel;
        this.purpose = purpose;

    }


    public static Boolean isUserDataCorrect(User user) {
        if (user.getName() == null || user.getName() == "")
            return false;
        if (user.getAge() < 0 || user.getAge() > 100)
            return false;
        if (!(user.getGender() == User.MALE || user.getGender() == User.FEMALE))
            return false;
        if (user.getActivityLevel() < User.SEDENTARY || user.getActivityLevel() > User.VERYACTIVE)
            return false;
        if (user.getPurpose() < User.DIET || user.getPurpose() > BULK)
            return false;
        if (user.getHeight() < 0 || user.getHeight() > 300)
            return false;
        if (user.getWeight() < 0 || user.getWeight() > 300)
            return false;
        if (user.getTargetWeight() < 0 || user.getTargetWeight() > 300)
            return false;
        return true;
    }

    public static String purposeToString(int purpose) {
        switch (purpose) {
            case DIET:
                return "다이어트";
            case BULK:
                return "벌크업";
            case MAINTAIN:
                return "체중 유지";
            default:
                return "오류";
        }
    }

    public String purposeToString() {
        return User.purposeToString(this.purpose);
    }

    @Override
    public String toString() {
        return "userIndex = " + userIndex + " + User {" + "name = " + name + "height = " + height + "weight = " + weight +
                "targetWeight = " + targetWeight + "age = " + age + "gender = " + gender +
                "activityLevel = " + activityLevel + "purpose = " + purpose + "bmr = " + bmr + "}";
    }

    public Long getUserIndex() { return userIndex; }
    public void setUserIndex(Long userIndex) { this.userIndex = userIndex; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Float getHeight() { return height; }
    public void setHeight(Float height) { this.height = height; }
    public Float getWeight() { return weight; }
    public void setWeight(Float weight) { this.weight = weight; }
    public Float getTargetWeight() { return targetWeight; }
    public void setTargetWeight(Float targetWeight) { this.targetWeight = targetWeight; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Integer getGender() { return gender; }
    public void setGender(Integer gender) {this.gender = gender;}
    public Integer getActivityLevel() { return activityLevel; }
    public void setActivityLevel(Integer activityLevel) { this.activityLevel = activityLevel; }
    public Integer getPurpose() { return purpose; }
    public void setPurpose(Integer purpose) { this.purpose = purpose; }
    public Float getBmr() { return bmr; }
    public void setBmr(Float bmr) { this.bmr = bmr; }
}
