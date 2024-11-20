package com.anonymous.mealmate.view.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.anonymous.mealmate.databinding.DialogPresetbringBinding;


import com.anonymous.mealmate.viewmodel.MealSetViewModel;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoadPresetDialogFragment extends DialogFragment {
    private MealSetViewModel mealSetViewModel;
    private DialogPresetbringBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mealSetViewModel = new ViewModelProvider(this).get(MealSetViewModel.class);
        binding = DialogPresetbringBinding.inflate(inflater, container, false);
        binding.setMealSetViewModel(mealSetViewModel);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mealSetViewModel.getPresetNameList());
        binding.lvPresetBringSelect.setAdapter(mAdapter);

        binding.btnDialogFinish.setOnClickListener(view -> {
            dismiss();
        });

        binding.lvPresetBringSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedString = (String) parent.getItemAtPosition(position);
                mealSetViewModel.onLoadPreset(selectedString);
                dismiss();
            }
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
