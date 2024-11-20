package com.anonymous.mealmate.view.adapter;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.mealmate.R;
import com.anonymous.mealmate.databinding.AdapterFoodBinding;
import com.anonymous.mealmate.databinding.AdapterMealitemBinding;
import com.anonymous.mealmate.databinding.FragmentFoodBinding;
import com.anonymous.mealmate.model.dto.FoodDto;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.view.activity.FoodActivity;
import com.anonymous.mealmate.viewmodel.FoodViewModel;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

// Todo 생성자를 context만  매개변수로 받기 -> complete
// Todo ViewModel 을 interface 화 하여 apdater 에서 변수병을 interfaece로 교체 , 다형성 및 재사용성 확보

public class FoodAdapter extends ListAdapter<FoodDto,FoodAdapter.FoodViewHolder>{

    private FoodViewModel foodViewModel;
    private MealSetViewModel mealSetViewModel;
    private LifecycleOwner lifecycleOwner;

    @Inject
    public FoodAdapter (LifecycleOwner lifecycleOwner, FoodViewModel foodViewModel, MealSetViewModel mealSetViewModel){
        super(new DiffUtil.ItemCallback<FoodDto>() {
            @Override
            public boolean areItemsTheSame(@NonNull FoodDto oldItem, @NonNull FoodDto newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull FoodDto oldItem, @NonNull FoodDto newItem) {
                return false;
            }
        });
        // ViewModel contact
        this.lifecycleOwner = lifecycleOwner;
        this.foodViewModel = foodViewModel;
        this.mealSetViewModel = mealSetViewModel;
    }

    // ViewHolder 클래스. Food 객체를 바인딩함
    public static class FoodViewHolder extends RecyclerView.ViewHolder{
        private AdapterFoodBinding binding;

        public FoodViewHolder(@NonNull AdapterFoodBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bind(FoodDto foodDto){
            binding.setFoodDto(foodDto);
        }
        public AdapterFoodBinding getBinding() {
            return binding;
        }
    }

    // ViewHolder 생성
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterFoodBinding binding = AdapterFoodBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        //생성한 바인딩에 뷰모델과 라이프사이클을 설정
        binding.setLifecycleOwner(lifecycleOwner);
        binding.setFoodViewModel(foodViewModel);
        binding.setMealSetViewModel(mealSetViewModel);

        return new FoodViewHolder(binding);
    }

    // ViewHolder에 데이터를 바인딩하는 메소드
    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodDto currentFoodDto = getItem(position);
        holder.bind(currentFoodDto);
        //holder.getBinding().executePendingBindings();   // binding 을 즉시 실행
    }
}
