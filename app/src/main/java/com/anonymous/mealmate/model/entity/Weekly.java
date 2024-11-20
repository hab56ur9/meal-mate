package com.anonymous.mealmate.model.entity;

import android.util.Log;

import androidx.room.Entity;

@Entity
public class Weekly {
    private String date;
    private Float totalCalories;

    public Weekly() { }

    public Weekly(String date, Float totalCalories) {
        Log.e("Weekly", "Date: " + date + ", Calories: " + totalCalories); // 로그 추가

        this.date= date;
        this.totalCalories = totalCalories;
    }

    @Override
    public String toString() {
        return "Weekly{ " +
                "date= " + date + " 날짜에 " +
                "totalCalories=" + totalCalories +
                '}';
    }

    public Float getTotalCalories() {
        return totalCalories;
    }
    public void setTotalCalories(Float totalCalories) {
        this.totalCalories = totalCalories;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}