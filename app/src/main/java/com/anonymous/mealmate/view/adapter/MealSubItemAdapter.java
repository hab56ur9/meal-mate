package com.anonymous.mealmate.view.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.mealmate.databinding.AdapterMealsubitemBinding;
import com.anonymous.mealmate.model.dto.FoodDto;
import com.anonymous.mealmate.model.dto.MealDto;
import com.anonymous.mealmate.service.MealMateService;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;

import javax.inject.Inject;

public class MealSubItemAdapter extends ListAdapter<FoodDto, MealSubItemAdapter.MealSubItemViewHolder> {

    private LifecycleOwner lifecycleOwner;
    private MealSetViewModel mealSetViewModel;

    // 에러방지용 초기값
    private MealDto mealDto = null;

    public void setMealDto(MealDto mealDto) {
        this.mealDto = mealDto;
    }

    MealMateService mealMateService = new MealMateService();

    public static class MealSubItemViewHolder extends RecyclerView.ViewHolder {

        private AdapterMealsubitemBinding binding;

        public MealSubItemViewHolder(@NonNull AdapterMealsubitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FoodDto foodDto) {
            binding.setFoodDto(foodDto);
        }
    }

    @Inject
    public MealSubItemAdapter(MealSetViewModel mealSetViewModel,LifecycleOwner lifecycleOwner) {
        super(new DiffUtil.ItemCallback<FoodDto>() {
            @Override
            public boolean areItemsTheSame(@NonNull FoodDto oldItem, @NonNull FoodDto newItem) {
                // todo 정확한 비교 수정필요
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull FoodDto oldItem, @NonNull FoodDto newItem) {
                // 정확한 비교 수정필요
                return false;
            }
        });
        this.lifecycleOwner = lifecycleOwner;
        this.mealSetViewModel =mealSetViewModel;
    }

    @NonNull
    @Override
    public MealSubItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AdapterMealsubitemBinding binding = AdapterMealsubitemBinding.inflate(layoutInflater, parent, false);
        binding.setLifecycleOwner(lifecycleOwner);
        binding.setMealSetViewModel(mealSetViewModel);

        // 식단정보를 초기값으로 세팅
        binding.setMealDto(mealDto);
        binding.setHiddenView(binding.clHidden);

        return new MealSubItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MealSubItemViewHolder holder, int position) {
        FoodDto currentFoodDto = getItem(position);
        holder.bind(currentFoodDto);
        holder.binding.setPosition(position);
        holder.binding.setAdapter(this);
    }
    public void addAmount(FoodDto foodDto, Integer position ){
        foodDto.addAmount();
        notifyItemChanged(position);
        activeNotifyCallBack();
    }
    public void minusAmount(FoodDto foodDto,Integer position){
        foodDto.minusAmount();
        notifyItemChanged(position);
        activeNotifyCallBack();
    }
    public void deleteItem(MealDto mealDto,FoodDto foodDto, Integer position){
        mealMateService.of(mealDto).remove(foodDto);
        notifyItemRemoved(position);
        activeNotifyCallBack();
    }
    public void onShowHiddenView(View view) {
        view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    private NotifyCallBack notifyCallBack;
    public interface NotifyCallBack{
        void onDataChanged();
    }
    public void setNotifyCallBack(NotifyCallBack notifyCallBack){
        this.notifyCallBack =notifyCallBack;
    }
    private void activeNotifyCallBack(){
        if(notifyCallBack != null)
            notifyCallBack.onDataChanged();
    }
}
