package com.anonymous.mealmate.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import com.anonymous.mealmate.R;

import com.anonymous.mealmate.databinding.ActivityMainBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.feature.Date;
import com.anonymous.mealmate.view.dialog.PurposeDialogFragment;
import com.anonymous.mealmate.view.dialog.WeightSettingDialogFragment;
import com.anonymous.mealmate.view.fragment.CalendarFragment;
import com.anonymous.mealmate.view.fragment.FoodFragment;
import com.anonymous.mealmate.view.fragment.HomeFragment;
import com.anonymous.mealmate.view.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    @Inject
    Date date;
    @Inject
    ControlViewState controlViewState;
    private ActivityMainBinding binding;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment;
    private CalendarFragment calendarFragment;
    private FoodFragment foodFragment;
    private UserFragment userFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // binding setup part
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        //fragment connecting part
        homeFragment = new HomeFragment();
        calendarFragment = new CalendarFragment();
        foodFragment = new FoodFragment();
        userFragment = new UserFragment();
        // 초기 실행 프래그먼트, 홈화면
        replaceFragment(homeFragment);

        /**
         *  BottomNavigation View EventListener
         *  fragment replace logic coded
         */
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.item_navigation_home:
                        replaceFragment(homeFragment);
                        date.setToDayDateTime();
                        return true;
                    case R.id.item_navigation_calendar:
                        replaceFragment(calendarFragment);
                        return true;
                    case R.id.item_navigation_food:
                        replaceFragment(foodFragment);
                        return true;
                    case R.id.item_navigation_user:
                        replaceFragment(userFragment);
                        return true;
                    default:
                        return false;
                }
        });
        //todo  배경화면 null 처리 , 이후 xml 에서 코드 작성 필요

        binding.bottomNavigationView.setItemIconTintList(null);

        /**
         * ControlViewState active intent signal
         * 데이터 전달 로직이 아니 므로 간단 하게  event listener 작성 하여 view state handling
         *
         * 플로팅 버튼 눌러 SetMealActivity intent
         */
        binding.fabMealModify.setOnClickListener((v) -> {
            controlViewState.activeIntentSignal(ControlViewState.INTENT_MAIN_TO_SETMEAL);
        });

        /**
         * Signal that ControlViewState send
         * get intent signal from ControlViewState class livedata
         * active intent another activity by intent signal
         */
        controlViewState.getStateSignalLiveData().observe(this, this::handleStateSignals);


        controlViewState.getStateSignalLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer dialogSignal) {
                switch (dialogSignal) {
                    //Todo dialog 작성하여 연결
                    case ControlViewState.DIALOG_FOOD_DATASET:
                }
            }
        });
    }
    private void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.fl_navigation, fragment).commit();
    }

    private void handleStateSignals(Integer signal) {
        switch (signal) {
            case ControlViewState.INTENT_MAIN_TO_SETMEAL:
                startActivity(new Intent(MainActivity.this, SetMealItemActivity.class));
                break;
            case ControlViewState.INTENT_MAIN_TO_USERUPDATEDATA:
                startActivity(new Intent(MainActivity.this, UserDataUpdateActivity.class));
                break;
            case ControlViewState.DIALOG_FOOD_DATASET:
                //Todo dialog 작성하여 연결
                break;
            case ControlViewState.DIALOG_WEIGHT_CONFIG:
                new WeightSettingDialogFragment().show(getSupportFragmentManager(),"weight");
                break;
            case ControlViewState.DIALOG_PURPOSE_CONFIG:
                new PurposeDialogFragment().show(getSupportFragmentManager(),"purpose");
                break;
        }
    }
}
