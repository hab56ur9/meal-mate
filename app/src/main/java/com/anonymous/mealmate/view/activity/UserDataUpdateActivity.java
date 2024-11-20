package com.anonymous.mealmate.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anonymous.mealmate.databinding.ActivityUserdataupdateBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.model.entity.User;
import com.anonymous.mealmate.viewmodel.UserViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserDataUpdateActivity extends AppCompatActivity {

    private ActivityUserdataupdateBinding binding;

    private UserViewModel userViewModel;

    @Inject
    ControlViewState controlViewState;

    private ViewFlipper viewFlipper; // 뷰 전환

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        setupBinding();
        setupViewFlipper();
        setupControlViewStateObserver();
    }

    // 데이터 바인딩 설정
    private void setupBinding() {
        binding = ActivityUserdataupdateBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);  // 라이프사이클 소유자 설정
        binding.setUserViewModel(userViewModel);  // 뷰모델 설정
        binding.setUser(new User());
        setContentView(binding.getRoot());  // 바인딩 된 레이아웃을 루트 뷰로 설정

        //TODO 유저 정보 설정
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null) {
                    binding.setUser(user);
                }
            }
        });
    }

    // ViewFlipper를 설정하는 메소드
    private void setupViewFlipper() {
        viewFlipper = binding.vfSurvey;
        binding.btnBack.setOnClickListener((v) -> {
            finish();
        });
        binding.btnNext.setOnClickListener((v) -> { viewFlipper.showNext();
            if(viewFlipper.getDisplayedChild()==2)
                v.setVisibility(View.GONE);
        });

    }

    // 뷰 상태 관찰자를 설정하는 메소드
    private void setupControlViewStateObserver() {
        controlViewState.getStateSignalLiveData().observe(this, (signal) -> {
            // 상태값에 따라 액티비티 종료
            if (signal == ControlViewState.INTENT_USERUPDATEDATA_TO_MAIN) {
                startActivity(new Intent(UserDataUpdateActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
