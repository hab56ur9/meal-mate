package com.anonymous.mealmate.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;


import com.anonymous.mealmate.databinding.ActivitySetmealitemBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.feature.Date;
import com.anonymous.mealmate.view.adapter.MealItemAdapter;
import com.anonymous.mealmate.view.dialog.LoadPresetDialogFragment;
import com.anonymous.mealmate.view.dialog.MealFoodDialogFragment;
import com.anonymous.mealmate.view.dialog.SavePresetDialogFragment;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SetMealItemActivity extends AppCompatActivity {
    private ActivitySetmealitemBinding binding; // Binding class instance. xml과 연결된다.
    private MealSetViewModel mealSetViewModel; // ViewModel instance
    private RecyclerView mRecyclerView; // Meal 데이터 목록 RecyclerView
    private MealItemAdapter mAdapter;
    @Inject
    ControlViewState controlViewState;
    @Inject
    Date date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mealSetViewModel = new ViewModelProvider(this).get(MealSetViewModel.class);
        setupBinding(); // data binding setup part
        setupRecyclerView(); // RecyclerView and adapter setup part
        setupObserver(); // ViewModel LiveData observer
        setupListeners(); // Event listeners
        mealSetViewModel.onLoadMeal();
    }


    // DataBinding을 설정.
    private void setupBinding() {
        binding = ActivitySetmealitemBinding.inflate(LayoutInflater.from(this));
        binding.setMealSetViewModel(mealSetViewModel);
        binding.setDate(date);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
    }

    // RecyclerView와 Adapter를 연결.
    private void setupRecyclerView() {
        mAdapter = new MealItemAdapter(mealSetViewModel, binding.getLifecycleOwner());
        mRecyclerView = binding.rvMealSetting;
        mRecyclerView.setAdapter(mAdapter);
    }

    // 뷰모델의 LiveData 객체를 관찰하고, 데이터가 변경되면 RecyclerView에 적용.
    private void setupObserver() {
//        mealSetViewModel.getMealSetViewModelSignal().observe(this, signal -> {
//            mAdapter.submitList(mealSetViewModel.getMealDtoList());
//            //showToastMessage("Food CallBack");
//        });
        mealSetViewModel.getMealDtoListLiveData().observe(this, mealDtoList -> {
            mAdapter.submitList(mealDtoList);
            mealSetViewModel.setMealDtoList();
        });

        // 뷰의 상태를 관찰하고, 상태가 변경되면 이벤트를 처리.
        controlViewState.getStateSignalLiveData().observe(this, this::handleStateSignals);
    }

    // 클릭하면 이전 화면으로 돌아감.
    private void setupListeners() {
        binding.btnMealBack.setOnClickListener((v) -> {
            finish();
        });
        binding.btnMealCalendar.setOnClickListener( v -> {
            binding.llMealCalendar.setVisibility(binding.llMealCalendar.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
        });
    }

    // 뷰의 상태에 따라 이벤트를 처리.
    private void handleStateSignals(Integer signal) {
        switch (signal) {
            case ControlViewState.INTENT_SETMEAL_TO_FOOD:
                startActivity(new Intent(SetMealItemActivity.this, FoodActivity.class));
                break;
            case ControlViewState.ACTIVITY_FINISH_SETMEAL:
                finish();
                break;
            case ControlViewState.DIALOG_MEAL_SAVE_PRESET:
                new SavePresetDialogFragment().show(getSupportFragmentManager(), "SaveDialog");
                break;
            case ControlViewState.DIALOG_MEAL_LOAD_PRESET:
                new LoadPresetDialogFragment().show(getSupportFragmentManager(), "LoadDialog");
                break;
            case ControlViewState.DIALOG_MEALFOOD_DATASET:
                new MealFoodDialogFragment().show(getSupportFragmentManager(),"MealFoodDialog");
        }
    }

    // Utility method for showing toast messages
    private void showToastMessage(String message) {
        Toast.makeText(SetMealItemActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
