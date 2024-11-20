package com.anonymous.mealmate.view.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.mealmate.R;
import com.anonymous.mealmate.databinding.FragmentFoodBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.view.adapter.FoodAdapter;
import com.anonymous.mealmate.view.dialog.FoodDialogFragment;
import com.anonymous.mealmate.viewmodel.FoodViewModel;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * class info :
 * Fragment class , MVVM 패턴 적용, data binding 사용, observer 사용
 * observer 를 사용 하여 data 변경 감지 -> adapter 에 갱신 신호를 보낸다.
 * view control 은 activity 의 observer 에서 trigger 사용 하여 일괄 처리
 * <p>
 * how to coding  :
 * data handle logic 은 xml 에서 viewModel method 직접 호출 하므로
 * Fragment , Activity 에서는 View inflate Control logic 만 작성 ex) fragment, intent , dialog
 * <p>
 * onCreateView method :
 * activity activity 의 정보를 받아  ex) inflater, container, savedInstanceState
 * View binding 을 처리 하여 return , activity 에 inflate
 * <p>
 * features :
 * 두가지 의  observer 가 하나의 adapter 에 변경 신호 {ex) submitList} 전송중
 * 같은 뷰에 성격이 다른 { ex) data from db , data form api } data 전송중
 */

@AndroidEntryPoint
public class FoodFragment extends Fragment {

    private FoodAdapter mSearchAdapter;
    private FoodAdapter mLikeAdapter;
    private FragmentFoodBinding binding;

    private FoodViewModel foodViewModel;
    private MealSetViewModel mealSetViewModel;

    @Inject
    ControlViewState controlViewState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View binding 을 통하여 view inflate
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        mealSetViewModel = new ViewModelProvider(this).get(MealSetViewModel.class);

        binding = FragmentFoodBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setFoodViewModel(foodViewModel);

        // RecyclerView setting
        mSearchAdapter = new FoodAdapter(this, foodViewModel, mealSetViewModel);
        mLikeAdapter = new FoodAdapter(this, foodViewModel, mealSetViewModel);
        //recyclerView , adapter setting part

        binding.rvFoodSearch.setAdapter(mSearchAdapter);
        binding.rvFoodLike.setAdapter(mLikeAdapter);

        foodViewModel.getFoodDtoListLiveData().observe(this, foodDtoList -> {
            mSearchAdapter.submitList(foodDtoList);
        });
        foodViewModel.getFoodDtoListLivedataWithLikeState().observe(this, foodDtoList -> {
            mLikeAdapter.submitList(foodDtoList);
        });

        binding.btnFoodCategoryAll.setOnClickListener( v -> {
            binding.vfFoodInfo.setDisplayedChild(0);
//            v.setBackground(v.getBackground()?R.drawable.btn_food_category_selected:R.drawable.btn_food_category);
        });
        binding.btnFoodCategoryLike.setOnClickListener( v -> {
            binding.vfFoodInfo.setDisplayedChild(1);
        });
        controlViewState.getStateSignalLiveData().observe(getViewLifecycleOwner(), signal -> {
            switch(signal){
                case ControlViewState.DIALOG_FOOD_DATASET: new FoodDialogFragment().show(getChildFragmentManager(),"FoodDialogFragment");
            }
        });


        // ViewBinding을 완료한 View를 반환
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        foodViewModel.onLoadFoodData();
    }
}