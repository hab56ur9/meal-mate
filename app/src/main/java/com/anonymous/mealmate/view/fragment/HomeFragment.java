package com.anonymous.mealmate.view.fragment;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.anonymous.mealmate.R;
import com.anonymous.mealmate.databinding.FragmentHomeBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.feature.Date;

import com.anonymous.mealmate.model.entity.User;
import com.anonymous.mealmate.view.adapter.MealAdapter;
import com.anonymous.mealmate.viewmodel.MealCheckViewModel;
import com.anonymous.mealmate.viewmodel.UserViewModel;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * class info :
 * Fragment class , MVVM 패턴 적용, data binding 사용, observer 사용
 * observer 를 사용 하여 data 변경 감지 -> adapter 에 갱신 신호를 보낸다.
 * view control 은 activity 의 observer 에서 trigger 사용 하여 일괄 처리
 * <p>
 * how to coding  :
 * data handle logic 은 xml 에서 viewModel method 직접 호출 하므로
 * Fragment , Activity 에서는 View inflate Control logic 만 작성 ex) fragment, intent , dialog
 * <p>
 * onCreateView method :
 * activity activity 의 정보를 받아  ex) inflater, container, savedInstanceState
 * View binding 을 처리 하여 return , activity 에 inflate
 * <p>
 * features :
 */

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MealAdapter mAdapter;
    private MealCheckViewModel mealCheckViewModel;

    private UserViewModel userViewModel;
    private User user;
    @Inject
    Date date;
    @Inject
    ControlViewState controlViewState;

    //뷰를 인플레이팅 해줍니다.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mealCheckViewModel = new ViewModelProvider(this).get(MealCheckViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // binding , viewModel setting part
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setMealCheckViewModel(mealCheckViewModel);


        binding.setDate(date); // 오늘 날짜로 기본 세팅

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            binding.setUser(user);
            this.user = user;
        });

        userViewModel.getWeeklyTotalCaloriesLiveData().observe(getViewLifecycleOwner(), calorieData -> {
            Log.e("HomeFragment", "getWeeklyTotalCaloriesLiveData" + calorieData.toString());

            List<BarEntry> entries = new ArrayList<>();
            Float maxCalories= 0f;

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date date2 = format.parse(date.getDateToString());
                Log.e("HomeFragment", "date2" + date2.toString());

                calendar.setTime(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int today = calendar.get(Calendar.DAY_OF_WEEK);
            String[] days = new String[]{"일", "토", "금", "목", "수", "화", "월"};

            String[] rotatedDays = rotate(days, today-Calendar.SUNDAY);
            if(user!=null){
                maxCalories = user.getBmr();}

            Log.e("HomeFragment", "maxCalories" + maxCalories.toString());

            int j =0;
            for (int i = calorieData.size()-1; i >=0 ; i--) {
                // Weekly 객체에서 totalCalories를 가져와 BarEntry를 생성합니다.

                entries.add(new BarEntry(j++, (calorieData.get(i).getTotalCalories() / maxCalories) * -1));
            }
            // 모든 데이터를 처리한 후에 setChart를 호출합니다.
            setChart(entries, rotatedDays);

        });

        //chart 설정
        //setChart();
        return binding.getRoot();
    }

    //뷰가 인플레이팅 된 후에 호출됩니다.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecyclerView와 Adapter 설정
        mAdapter = new MealAdapter(mealCheckViewModel, getViewLifecycleOwner());
        binding.includeMealList.rvMealLoading.setAdapter(mAdapter);
        // recycler view set end

        // ViewModel에서 변화를 감지하여 리스트를 업데이트 합니다.
        mealCheckViewModel.getMealDtoListLiveData().observe(getViewLifecycleOwner(), mealDtoList ->{
            mAdapter.submitList(mealDtoList);
        });
    }

    // 사용자가 프래그먼트 와 상호 작용이 가능할 때 호출됩니다. transaction 이후
    @Override
    public void onResume() {
        super.onResume();
        //뷰모델에 데이터 로드 신호를 보냅니다.
        mealCheckViewModel.onLoadMeal();

    }

    @Override
    public void onStart() {
        super.onStart();
        userViewModel.getWeeklyTotalCalories();
    }

    // 문자열 배열을 회전시키는 메서드
    private String[] rotate(String[] array, int amount) {
        int n = array.length;
        String[] rotated = new String[n];
        for (int i = 0; i < n; i++) {
            rotated[(i + amount) % n] = array[i];
        }
        return rotated;
    }
    public void setChart(List<BarEntry> entries, String[] days) {
        // 바 차트
        HorizontalBarChart barChart = binding.hbcMealChart;

        BarDataSet dataSet = new BarDataSet(entries, "칼로리 달성률");

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f%%", value * -100); // value에 100을 곱하여 백분율로 표시
            }
        });

        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setTextColor(R.color.black_40);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return days[(int) value];
            }
        });

        xAxis.setDrawGridLines(false); // X축 레이블 활성화
        xAxis.setDrawAxisLine(false);

        YAxis yAxis = barChart.getAxisLeft(); // Y축을 가져옴
        yAxis.setEnabled(false);
        LimitLine ll = new LimitLine(1f, "Target"); // 1의 위치에 가이드라인을 추가
        ll.setLineColor(Color.RED);
        ll.setLineWidth(2f);
        yAxis.addLimitLine(ll); // 가이드라인을 Y축에 추가

        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false); // 차트 설명 비활성화
        barChart.animateY(1000); // 애니메이션 효과 적용

        barChart.invalidate(); // 차트 갱신
    }


//    public void setChart() {
//        // 바 차트
//        HorizontalBarChart barChart = binding.hbcMealChart;
//
//        BarEntry ex = new BarEntry(0, -2000f);
////        ex.setIcon(getResources().getDrawable(R.drawable.icon_language));
//        List<BarEntry> entries = new ArrayList<>();
//        entries.add(ex);
//        entries.add(new BarEntry(1f, -1800f));
//        entries.add(new BarEntry(2f, -1800f));
//        entries.add(new BarEntry(3f, -2200f));
//        entries.add(new BarEntry(4f, -1900f));
//        entries.add(new BarEntry(5f, -2400f));
//        entries.add(new BarEntry(6f, -2300f));
//
//        BarDataSet dataSet = new BarDataSet(entries, "칼로리 달성률");
//
//        BarData barData = new BarData(dataSet);
//        barData.setBarWidth(0.7f);
//
//        barChart.setData(barData);
//
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.TOP);
//        xAxis.setTextColor(R.color.black_40);
//
//        xAxis.setDrawGridLines(false); // X축 레이블 활성화
//        xAxis.setDrawAxisLine(false);
//
//        barChart.getAxisLeft().setEnabled(false);
//        barChart.getAxisRight().setEnabled(false);
//        barChart.getDescription().setEnabled(false); // 차트 설명 비활성화
//        barChart.animateY(1000); // 애니메이션 효과 적용
//
//        barChart.invalidate(); // 차트 갱신
//    }

}

