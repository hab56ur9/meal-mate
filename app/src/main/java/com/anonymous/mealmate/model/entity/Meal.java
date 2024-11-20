package com.anonymous.mealmate.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import javax.annotation.Nullable;
import javax.inject.Inject;

@Entity(tableName = "meal", indices = {@Index("mealDate")})
public class Meal {
    //0529 backend 통합완료

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="mealIndex")
    private Long mealIndex;
    private String mealDate;
    private Integer mealCnt;
    private Integer checked = UNCHECKED;

    private String mealPreset=null;

    @Ignore
    public static final int CHECKED = 1;
    @Ignore
    public static final int UNCHECKED = 0;


    @Ignore
    public Meal(@Nullable Long mealIndex, String mealDate, Integer mealCnt, Integer checked) {
        this.mealIndex = mealIndex;
        this.mealDate = mealDate;
        this.mealCnt = mealCnt;
        this.checked = checked;
    }
    //룸에서 사용하는 생성자
    public Meal(String mealDate, Integer mealCnt, Integer checked,String mealPreset) {
        this.mealDate = mealDate;
        this.mealCnt = mealCnt;
        this.checked = checked;
        this.mealPreset= mealPreset;
    }

    //insert 용 Meal 생성자, Index와 preset을 null로 두어 추출.
    @Ignore
    public Meal(@NonNull  String mealDate, @NonNull  Integer mealCnt, @NonNull Integer checked){
        this.mealDate = mealDate;
        this.mealCnt = mealCnt;
        this.checked = checked;
    }
    //프리셋용 임시 Meal 생성자  date 정보가 null 이므로 날짜에 의해 검색이 안됨.
    @Ignore
    public Meal(@NonNull  Integer mealCnt,@NonNull  Integer checked, @NonNull String mealPreset){
        this.mealPreset = mealPreset;
        this.mealCnt =mealCnt;
        this.checked=checked;
    }

    // getter and setter methods...
    public Long getMealIndex() {
        return mealIndex;
    }

    public void setMealIndex(Long mealIndex) {
        this.mealIndex = mealIndex;
    }

    public String getMealDate() {
        return mealDate;
    }

    public void setMealDate(String mealDate) {
        this.mealDate = mealDate;
    }

    public Integer getMealCnt() {
        return mealCnt;
    }

    public void setMealCnt(int mealNum) {
        this.mealCnt = mealNum;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public Integer getChecked() {
        return checked;
    }

    public String getMealPreset() {
        return mealPreset;
    }

    public void setMealPreset(String mealPreset) {
        this.mealPreset = mealPreset;
    }
}