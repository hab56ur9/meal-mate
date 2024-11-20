package com.anonymous.mealmate.feature;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.lang.Integer;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Date { //date entity  do not erase

    // 싱글톤 적용하여 하나의 날짜를 모든 클래스에서 사용할 필요성이 있음 static으로 구현
    private static Date instance = null;
    private final static int ERROR_CODE = -1;

    private int year = ERROR_CODE;
    private int month = ERROR_CODE;
    private int dayOfMonth = ERROR_CODE;
    private int dayOfWeek = ERROR_CODE;

    private String dateToString = null;
    private String dayOfWeekToString = null;

    private List<String> WeekToString = new ArrayList<>();

    public final static int WEEK_SUNDAY = 1;
    public final static int WEEK_MONDAY = 2;
    public final static int WEEK_TUESDAY = 3;
    public final static int WEEK_WEDNESDAY = 4;
    public final static int WEEK_THURSDAY = 5;
    public final static int WEEK_FRIDAY = 6;
    public final static int WEEK_SATURDAY = 7;

    @Inject
    public Date() { // 초기 실행 위한 생성자, 오늘 날짜로 초기화 해줌
        setToDayDateTime();
        syncDateVariable();
    }
    private void syncDateVariable() {
        //DB업로드 양식 저장
        setDateToString();
        //요일을 문자열로 저장
        setDayOfWeekToString();
        //라이브 데이터 변경 -> 옵저버 작동
        activeDateChangedSignal();
    }

    // 두가지 값만 가지게 하여 옵저버가 관측 on,off 옵저버 작동 만을 위한 목적 이기에 boolean 도 효율적 이지 않을 것
    private MutableLiveData<Integer> dateChangedSignalLiveData = new MutableLiveData<>(0);

    public LiveData<Integer> getDateChangedSignalLiveData() {
        return dateChangedSignalLiveData;
    }
    private void activeDateChangedSignal() {
        dateChangedSignalLiveData.setValue(dateChangedSignalLiveData.getValue().intValue() == 1 ? 0 : 1);
    }

    public void set(int year, int month, int dayOfMonth, int dayOfWeek) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;

        syncDateVariable();
    }
    public Date setToDayDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        syncDateVariable();

        return this;
    }

    public String getDayOfWeekToString() {
        return dayOfWeekToString;
    }
    private Date setDayOfWeekToString() {
        switch (dayOfWeek) {
            case WEEK_SUNDAY:
                dayOfWeekToString = "일";
                break;
            case WEEK_MONDAY:
                dayOfWeekToString = "월";
                break;
            case WEEK_TUESDAY:
                dayOfWeekToString = "화";
                break;
            case WEEK_WEDNESDAY:
                dayOfWeekToString = "수";
                break;
            case WEEK_THURSDAY:
                dayOfWeekToString = "목";
                break;
            case WEEK_FRIDAY:
                dayOfWeekToString = "금";
                break;
            case WEEK_SATURDAY:
                dayOfWeekToString = "토";
                break;
            case ERROR_CODE:
                dayOfWeekToString = "None";
                break;
        }
        return this;
    }
    public String getDateToString() {
        return dateToString;
    }
//    private Date setDateToString() {
//        if (month != ERROR_CODE && dayOfMonth != ERROR_CODE && dayOfWeek != ERROR_CODE) {
//           // SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
//            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
//            dateToString = format.format(new java.util.Date(year, month, dayOfMonth));
//        }
//        return this;
//    }
    private Date setDateToString() {
        if (month != ERROR_CODE && dayOfMonth != ERROR_CODE && dayOfWeek != ERROR_CODE) {
            //SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            dateToString = format.format(calendar.getTime());
        }
        return this;
    }

    //startDate가 2020-05-07이라면 endDate는 2020-05-01이어야 함
    public String getEndDateToString(String startDate) {
        String[] split = startDate.split("-");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int dayOfMonth = Integer.parseInt(split[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        calendar.add(Calendar.DATE, -6);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(calendar.getTime());

    }

    // getter , setter
    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }
    public int getDayOfMonth() {
        return dayOfMonth;
    }
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public Date setYear(int year) {
        this.year = year;
        syncDateVariable();
        return this;
    }
    public Date setMonth(int month) {
        this.month = month;
        syncDateVariable();
        return this;
    }
    public Date setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        syncDateVariable();
        return this;
    }
    public Date setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        syncDateVariable();
        return this;
    }
    public void setWeekToString(){
//        List<String> StringList= new ArrayList<>();
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        calendar.getw
//
//        String dateToString;
//        if (month != ERROR_CODE && dayOfMonth != ERROR_CODE && dayOfWeek != ERROR_CODE) {
//            SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
//            dateToString = format.format(new java.util.Date(year, month, dayOfMonth));
//        }
    }
}