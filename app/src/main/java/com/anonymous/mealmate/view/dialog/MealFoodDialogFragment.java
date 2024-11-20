package com.anonymous.mealmate.view.dialog;

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
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.anonymous.mealmate.databinding.DialogSubiteminfoBinding;

import com.anonymous.mealmate.viewmodel.MealSetViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MealFoodDialogFragment extends DialogFragment {

    private MealSetViewModel mealSetViewModel;
    private DialogSubiteminfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mealSetViewModel = new ViewModelProvider(this).get(MealSetViewModel.class);
        binding = DialogSubiteminfoBinding.inflate(inflater,container,false);
        binding.setFoodDto(mealSetViewModel.getSelectedFoodDto());
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
