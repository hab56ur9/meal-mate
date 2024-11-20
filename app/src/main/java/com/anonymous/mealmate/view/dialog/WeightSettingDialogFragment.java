package com.anonymous.mealmate.view.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anonymous.mealmate.databinding.DialogWeightBinding;
import com.anonymous.mealmate.model.entity.User;
import com.anonymous.mealmate.viewmodel.UserViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WeightSettingDialogFragment extends DialogFragment {

    private DialogWeightBinding binding;
    private UserViewModel userViewModel;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogWeightBinding.inflate(inflater,container,false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        binding.setUserViewModel(userViewModel);

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {

            binding.setUser(user);
            this.user = user;
            Log.e("WeightSettingDialogFragment", "onCreateView: " + user.toString());
        });

        binding.btnDialogWeightCancle.setOnClickListener( v -> {
            dismiss();
        });
        binding.btnDialogWeightFinish.setOnClickListener( v -> {
            Log.e("WeightSettingDialogFragment", "onClick: " + userViewModel.getUser().getValue().toString());
            userViewModel.onInsertUserData(userViewModel.getUser().getValue());
            dismiss();
        });

        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(500, 300);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}
