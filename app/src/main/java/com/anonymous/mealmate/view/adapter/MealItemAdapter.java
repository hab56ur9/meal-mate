package com.anonymous.mealmate.view.adapter;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.ViewGroup;


import androidx.annotation.NonNull;

import androidx.lifecycle.LifecycleOwner;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.mealmate.databinding.AdapterMealitemBinding;
import com.anonymous.mealmate.model.dto.FoodDto;
import com.anonymous.mealmate.model.dto.MealDto;


import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.service.MealMateService;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class MealItemAdapter extends ListAdapter<MealDto, MealItemAdapter.MealItemViewHolder> {

    private MealSetViewModel mealSetViewModel;
    private LifecycleOwner lifecycleOwner;

    private MealMateService mealMateService = new MealMateService();
    @Inject
    public MealItemAdapter(@NonNull MealSetViewModel mealSetViewModel, @NonNull LifecycleOwner lifecycleOwner) {
        super(new DiffUtil.ItemCallback<MealDto>() {
            @Override // Todo 정확한 비교로직 작성 필요
            public boolean areItemsTheSame(@NonNull MealDto oldItem, @NonNull MealDto newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull MealDto oldItem, @NonNull MealDto newItem) {
                return false;
            }
        });
        this.mealSetViewModel = mealSetViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    public static class MealItemViewHolder extends RecyclerView.ViewHolder {

        // 이후 변수 변경이 없으므로 final 설정
        private final AdapterMealitemBinding binding;
        public MealItemViewHolder(@NonNull AdapterMealitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public AdapterMealitemBinding getBinding() {
            return binding;
        }
    }

    @NonNull
    @Override
    public MealItemAdapter.MealItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //초기 1회만 실행되서 ViewHolder 세팅시작하는 메서드
        //use context to binding setup
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AdapterMealitemBinding binding = AdapterMealitemBinding.inflate(layoutInflater, parent, false);
        binding.setLifecycleOwner(lifecycleOwner);
        binding.setMealSetViewModel(mealSetViewModel);

        return new MealItemViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MealItemAdapter.MealItemViewHolder holder, int position) {
        //item 별 구분되는 기능 list item 개수만큼 호출됨
        MealDto currentMealDto = getItem(position);
        holder.binding.setMealDto(currentMealDto);
        Log.e(getClass().getName(), "onBindViewHolder: size"+position );
        //item의 variable에 postion값을 저장시킴 이후 음식 추가에서 활용
        holder.getBinding().setPosition(position);
        //어댑터 생성하여 연결   // 에러 방지를 위해 onBindViewHolder에 작성
        MealSubItemAdapter mealSubItemAdapter = new MealSubItemAdapter(holder.binding.getMealSetViewModel(),holder.binding.getLifecycleOwner());
        //어댑터에 변수 전달
        mealSubItemAdapter.setMealDto(currentMealDto);
        holder.binding.rvMealSubItem.setAdapter(mealSubItemAdapter);

        mealSubItemAdapter.submitList(currentMealDto.getFoodDtoList());

        holder.binding.setAdapter(this);
        mealSubItemAdapter.setNotifyCallBack( () -> {
            mealMateService.of(currentMealDto).sync();
            notifyItemChanged(position);
        });
    }
    public void onDataChanged(Integer position){
        notifyItemChanged(position);
    }
}
