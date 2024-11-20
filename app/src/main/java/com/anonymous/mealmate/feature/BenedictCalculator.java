package com.anonymous.mealmate.feature;

import android.util.Log;

import com.anonymous.mealmate.model.entity.User;

public class BenedictCalculator {
    private float bmr;
    private User user;

    public static final double SEDENTARY = 1.2;
    public static final double LIGHT = 1.375;
    public static final double MODERATE = 1.55;
    public static final double ACTIVE = 1.725;
    public static final double VERYACTIVE = 1.9;
    public static final double DIET = 0.8;
    public static final double BULK = 1.2;
    public static final double MAINTAIN = 1.0;

    public BenedictCalculator() {
    }

    public float calculate(User user) {
        return calculateBasicBMR(user)
                .adjustForActivityLevel(user)
                .adjustForPurpose(user)
                .getBMR();
    }

    public BenedictCalculator calculateBasicBMR(User user) {

        // user가 잘 들어왔는지 확인
        Log.d("BenedictCalculator", "calculateBasicBMR: " + user.toString());

        if (user.getGender() == User.MALE) {
            this.bmr = (float) ((10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) + 5);
        } else {
            this.bmr = (float) ((10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) - 161);
        }

        Log.d("BenedictCalculator", "calculateBasicBMR: " + this.bmr);
        Log.d("BenedictCalculator", "calculateBasicBMR: " + user.toString());
        return this;
    }

    public BenedictCalculator adjustForActivityLevel(User user) {
        switch (user.getActivityLevel()) {
            case User.SEDENTARY:
                this.bmr *= SEDENTARY;
                break;
            case User.LIGHT:
                this.bmr *= LIGHT;
                break;
            case User.MODERATE:
                this.bmr *= MODERATE;
                break;
            case User.ACTIVE:
                this.bmr *= ACTIVE;
                break;
            case User.VERYACTIVE:
                this.bmr *= VERYACTIVE;
                break;
        }
        return this;
    }

    public BenedictCalculator adjustForPurpose(User user) {
        switch (user.getPurpose()) {
            case User.DIET:
                this.bmr *= DIET;
                break;
            case User.BULK:
                this.bmr *= BULK;
                break;
            case User.MAINTAIN:
                this.bmr *= MAINTAIN;
                break;
        }
        return this;
    }

    public float getBMR() {

        Log.d("BenedictCalculator", "getBMR: " + this.bmr);
        return this.bmr;
    }
}