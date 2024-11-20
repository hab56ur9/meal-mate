package com.anonymous.mealmate.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.viewmodel.UserViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashScreenActivity extends AppCompatActivity {

    UserViewModel userViewModel;
    @Inject
    ControlViewState controlViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlViewState.getStateSignalLiveData().observe(this, this::handleStateSignals);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        userViewModel.checkUserData();
    }

    private void handleStateSignals(Integer signal) {
        switch (signal) {
            case ControlViewState.INTENT_SPALSH_TO_USERUPDATEDATA:
                startActivity(new Intent(SplashScreenActivity.this, UserDataUpdateActivity.class));
                finish();
                break;
            case ControlViewState.INTENT_SPLASH_TO_MAIN:
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
                break;
        }
    }
}