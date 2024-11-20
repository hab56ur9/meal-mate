package com.anonymous.mealmate.view.dialog;

import android.app.Application;
import android.app.Dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.anonymous.mealmate.databinding.DialogFoodBinding;
import com.anonymous.mealmate.databinding.DialogFoodinfoBinding;
import com.anonymous.mealmate.feature.MealMateApplication;
import com.anonymous.mealmate.viewmodel.FoodViewModel;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FoodDialogFragment extends DialogFragment{

    private FoodViewModel foodViewModel;

    private MealSetViewModel mealSetViewModel;
    private DialogFoodinfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        mealSetViewModel = new ViewModelProvider(this).get(MealSetViewModel.class);
        binding = DialogFoodinfoBinding.inflate(inflater,container,false);
        binding.setFoodViewModel(foodViewModel);
        binding.setMealSetViewModel(mealSetViewModel);
        binding.setFoodDto(FoodViewModel.getSelectedFood());
        binding.btnDialogFinish.setOnClickListener(view-> {
            dismiss();
        });

        return binding.getRoot();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(500, 300);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

}
