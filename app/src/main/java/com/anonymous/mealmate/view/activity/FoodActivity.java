package com.anonymous.mealmate.view.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.mealmate.databinding.ActivityFoodBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.view.adapter.FoodAdapter;
import com.anonymous.mealmate.view.dialog.FoodDialogFragment;
import com.anonymous.mealmate.viewmodel.FoodViewModel;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * class info :
 *  Activity class , MVVM 패턴 적용, data binding 사용, observer 사용
 *  observer 를 사용 하여 data 변경 감지 -> adapter 에 갱신 신호를 보낸다.
 *  view control 은 activity 의 observer 에서 trigger 사용 하여 일괄 처리
 *
 * how to coding  :
 *  data handle logic 은 xml 에서 viewModel method 직접 호출 하므로
 *  Fragment , Activity 에서는 View inflate Control logic 만 작성 ex) fragment, intent , dialog
 *
 * onCreate method :
 *  Android LifeCycle 에 의해 Control 호출, System 에 의해 관리 된다.
 *
 * features:
 *  1. ControlViewState 에서 신호를 받아 observe
 *      observer 사용 하여 수신 받은 signal 에 따라 다른 기능을 수행 하는 logic 작성
 *  -> View State Handling
 *
 *  2. 두가지 의  observer 가 하나의 adapter 에 변경 신호 {ex) submitList} 전송중
 *    -> 같은 뷰에 성격이 다른 { ex) data from db , data form api } data 전송중
 *
 * Note :!!
 *  if DataBinding enabled
 *  Android Studio auto generated Binding classes  ex) ActivityMainBinding.class , FragmentFoodBinding.class
 *  binding class need to set LifeCycleOwner and XML variable instances ex) ViewModel
 *  -> activity must setting these things
 *
 *  how to receive view that binding class owned , to inflate
 *  -> binding.getRoot() : View type
 *  setContentView(binding.getRoot());
 */

@AndroidEntryPoint // Hilt를 이용해 의존성 주입을 받을 수 있도록 한다.
public class FoodActivity extends AppCompatActivity {

    private ActivityFoodBinding binding;
    private FoodViewModel foodViewModel;
    private MealSetViewModel mealSetViewModel;
    private FoodAdapter mSearchAdapter;
    private FoodAdapter mLikeAdapter;
    @Inject
    ControlViewState controlViewState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        mealSetViewModel = new ViewModelProvider(this).get(MealSetViewModel.class);

        binding = ActivityFoodBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        binding.setFoodViewModel(foodViewModel);
        setContentView(binding.getRoot());

        mSearchAdapter = new FoodAdapter(this, foodViewModel, mealSetViewModel);
        mLikeAdapter = new FoodAdapter(this, foodViewModel, mealSetViewModel);
        //recyclerView , adapter setting part

        binding.rvFoodSearch.setAdapter(mSearchAdapter);
        binding.rvFoodLike.setAdapter(mLikeAdapter);

        foodViewModel.getFoodDtoListLiveData().observe(this, foodDtoList -> {
            Log.e(getClass().getName(), "onCreate: foodDto Site"+foodDtoList.size());
            mSearchAdapter.submitList(foodDtoList);
            Log.e(getClass().getName(), "onCreate:foodDto Site"+foodDtoList.size());
        });
        foodViewModel.getFoodDtoListLivedataWithLikeState().observe(this, foodDtoList -> {
            mLikeAdapter.submitList(foodDtoList);

        });

        binding.btnFoodCategoryAll.setOnClickListener( v -> {
            binding.vfFoodInfo.setDisplayedChild(0);
        });
        binding.btnFoodCategoryLike.setOnClickListener( v -> {
            binding.vfFoodInfo.setDisplayedChild(1);
        });

        controlViewState.getStateSignalLiveData().observe(this, signal -> {
            switch(signal){
                case ControlViewState.DIALOG_FOOD_DATASET:
                    new FoodDialogFragment().show(getSupportFragmentManager(),"FoodDialogFragment");break;
                case ControlViewState.ACTIVITY_FINISH_FOOD :
                    controlViewState.deactivateSignal();finish();break;
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        foodViewModel.onLoadFoodData();
    }
}
