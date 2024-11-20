package com.anonymous.mealmate.view.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.mealmate.databinding.AdapterMealBinding;

import com.anonymous.mealmate.model.dto.MealDto;

import com.anonymous.mealmate.viewmodel.MealCheckViewModel;


import javax.inject.Inject;


public class MealAdapter extends ListAdapter<MealDto, MealAdapter.MealViewHolder> {

    private MealCheckViewModel mealCheckViewModel;
    private LifecycleOwner lifecycleOwner;

    public static class MealViewHolder extends RecyclerView.ViewHolder {

        private AdapterMealBinding binding;

        public MealViewHolder(@NonNull AdapterMealBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MealDto mealDto) {
            binding.setMealDto(mealDto);
            binding.executePendingBindings(); // binding 을 즉시 실행. 이점 : view 가 즉시 업데이트 됨
        }

        private AdapterMealBinding getBinding() {
            return binding;
        }
    }

    @Inject
    public MealAdapter(MealCheckViewModel mealCheckViewModel, LifecycleOwner lifecycleOwner) {
        super(new DiffUtil.ItemCallback<MealDto>() {
            @Override
            public boolean areItemsTheSame(@NonNull MealDto oldItem, @NonNull MealDto newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull MealDto oldItem, @NonNull MealDto newItem) {
                return false;
            }
        });
        this.mealCheckViewModel = mealCheckViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }


    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdapterMealBinding binding = AdapterMealBinding.inflate(inflater,parent,false);
        binding.setLifecycleOwner(lifecycleOwner);
        binding.setMealCheckViewModel(mealCheckViewModel);

        return new MealViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealDto currentMealDto = getItem(position);
        //holder.bind(currentMealDto);
        holder.binding.setMealDto(currentMealDto);
        Log.e(getClass().getName(), "onBindViewHolder: test"+position);
    }
}

