package com.anonymous.mealmate.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.mealmate.R;
import com.anonymous.mealmate.databinding.FragmentCalendarBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.feature.Date;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.view.adapter.MealAdapter;
import com.anonymous.mealmate.viewmodel.MealCheckViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * class info :
 * Fragment class , MVVM 패턴 적용, data binding 사용, observer 사용
 * observer 를 사용 하여 data 변경 감지 -> adapter 에 갱신 신호를 보낸다.
 * view control 은 activity 의 observer 에서 trigger 사용 하여 일괄 처리
 *
 * how to coding  :
 * data handle logic 은 xml 에서 viewModel method 직접 호출 하므로
 * Fragment , Activity 에서는 View inflate Control logic 만 작성 ex) fragment, intent , dialog
 *
 * onCreateView method :
 *  activity activity 의 정보를 받아  ex) inflater, container, savedInstanceState
 *  View binding 을 처리 하여 return , activity 에 inflate
 *
 *  features :
 *  Date instance : 날짜 정보를 저장 하는 Singleton class Date 호출 하여 observer 를 통하여 view data update
 */

@AndroidEntryPoint
public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    private RecyclerView mRecyclerView;
    private MealAdapter mAdapter;
    private MealCheckViewModel mealCheckViewModel;
    @Inject
    Date date;
    @Inject
    ControlViewState controlViewState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mealCheckViewModel = new ViewModelProvider(this).get(MealCheckViewModel.class);

        // binding , viewModel setting part
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setMealCheckViewModel(mealCheckViewModel);
        binding.setDate(date);
        return binding.getRoot();
    }
//        binding.cvMealInfo.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//
//            }
//        });

//        binding.cvMealInfo.setTitleFormatter(new TitleFormatter() {
//            @Override
//            public CharSequence format(CalendarDay day) {
//                return day.getYear() + "년 " + day.getMonth() + "월";
//            }
//        });
//        binding.cvMealInfo.setWeekDayFormatter( new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));
//        // return view that binding setting completed
//


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // recyclerView , adapter setting part
        mAdapter = new MealAdapter(mealCheckViewModel, getViewLifecycleOwner());
        mRecyclerView=binding.includeMealList.rvMealLoading;
        mRecyclerView.setAdapter(mAdapter);

        // observer setting part : date 객체를 observe 하여 view를 update
        mealCheckViewModel.getMealDtoListLiveData().observe(getViewLifecycleOwner(), mealDtoList -> {
            mAdapter.submitList(mealDtoList);
            Log.e(getTag(), "onViewCreated: "+mealDtoList.size() );
        });

    }
    // 사용자가 프래그먼트 와 상호 작용이 가능할 때 호출됩니다. transaction 이후
    @Override
    public void onResume() {
        super.onResume();

        //뷰모델에 데이터 로드 신호를 보냅니다.
        mealCheckViewModel.onLoadMeal();
    }

}
