package com.anonymous.mealmate.model.repository;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.anonymous.mealmate.feature.Date;
import com.anonymous.mealmate.model.dao.JoinDao;
import com.anonymous.mealmate.model.database.AppDatabase;
import com.anonymous.mealmate.model.entity.JoinResult;
import com.anonymous.mealmate.model.entity.Weekly;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class JoinRepository {
    private AppDatabase appDatabase;
    private JoinDao joinDao;
    private Date date;

    @Inject
    public JoinRepository(AppDatabase appDatabase, JoinDao joinDao, Date date){
        this.appDatabase =appDatabase;
        this.joinDao=joinDao;
        this.date = date;
    }

    public List<JoinResult> getMealFoodWithFoodByMealIndex(Long mealIndex){
        return joinDao.getMealFoodWithFoodByMealIndex(mealIndex);
    }

    public List<Weekly> getWeeklyTotalCalories(){
        String endDate = date.getDateToString();
        Log.e("JoinRepository", "endDate: " + endDate);

        String startDate = date.getEndDateToString(endDate);
        Log.e("JoinRepository", "startDate: " + startDate);

        List<Weekly> weeklyList = joinDao.getWeeklyTotalCalories(startDate, endDate);
        for (Weekly weekly : weeklyList) {
            Log.e("DEBUG", "Date: " + weekly.getDate() + ", Calories: " + weekly.getTotalCalories());
        }
        Log.e("JoinRepository", "Weekly list: " + weeklyList.toString());

        // Map을 사용하여 date를 key로 하고 Weekly 객체를 value로 합니다. 이는 나중에 날짜별 Weekly 데이터를 빠르게 찾기 위한 것입니다.
        Map<String, Weekly> dateToWeekly = new HashMap<>();

        // SimpleDateFormat 객체를 생성합니다. 이 객체는 문자열을 날짜로 변환하고 날짜를 문자열로 변환하는 데 사용됩니다.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // 날짜 : 2021-05-01 형식

        try {
            // 시작 날짜와 끝 날짜를 Date 객체로 변환합니다.
            java.util.Date start = sdf.parse(startDate);
            java.util.Date end = sdf.parse(endDate);

            // Calendar 객체를 사용하여 기간 동안의 모든 날짜에 대해 반복합니다.
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            while (!calendar.getTime().after(end)) {
                // 현재 날짜를 문자열로 변환합니다.
                String currentDate = sdf.format(calendar.getTime());

                // 현재 날짜에 대해 0 칼로리의 Weekly 객체를 만들고 Map에 넣습니다.
                Weekly weekly = new Weekly(currentDate, 0f);
                dateToWeekly.put(currentDate, weekly);

                // 다음 날로 이동합니다.
                calendar.add(Calendar.DATE, 1);
            }

            // 쿼리로 가져온 데이터를 Map에 넣습니다. 이때, 같은 날짜의 데이터가 이미 있으면 새로운 데이터로 교체됩니다.
            for (Weekly weekly : weeklyList) {
                dateToWeekly.put(weekly.getDate(), weekly);
            }

            // Map에서 Weekly 객체를 가져와서 List에 넣습니다. 그리고 이 List를 날짜 순서대로 정렬합니다.
            List<Weekly> finalList = new ArrayList<>(dateToWeekly.values());
            finalList.sort(Comparator.comparing(Weekly::getDate));

            Log.e("JoinRepository", "Final weekly list: " + finalList.toString());

            // 최종적으로 모든 날짜에 대한 데이터를 가진 List를 반환합니다.
            return finalList;

        } catch (ParseException e) {
            // 날짜 변환 중 오류가 발생한 경우 처리합니다.
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
