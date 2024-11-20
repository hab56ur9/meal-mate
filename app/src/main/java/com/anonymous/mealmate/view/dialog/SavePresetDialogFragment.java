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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anonymous.mealmate.databinding.DialogPresetcreateBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SavePresetDialogFragment extends DialogFragment {
    private MealSetViewModel mealSetViewModel;
    private DialogPresetcreateBinding binding;

    @Inject
    ControlViewState controlViewState;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mealSetViewModel = new ViewModelProvider(this).get(MealSetViewModel.class);
        binding = DialogPresetcreateBinding.inflate(inflater, container, false);
        binding.setMealSetViewModel(mealSetViewModel);
        binding.setEditText(binding.etPresetCreate);
        binding.btnDialogCancle.setOnClickListener(view -> {
            dismiss();
        });

        return binding.getRoot();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        //dialog.getWindow().setLayout(500, 300);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }


}
