package com.anonymous.mealmate.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import javax.annotation.Nullable;

@Entity(tableName = "food")
public class Food {
    @ColumnInfo(name="foodIndex")
    @PrimaryKey(autoGenerate = true)
    private Long foodIndex;
    private String foodName;
    private Float food1serving;
    private Float foodKcal;
    private Float foodCarbohydrates;
    private Float foodProtein;
    private Float foodFat;
    private String food_company;
    private Integer foodLike;   // default value: 0

    @Ignore
    public static final int  FOOD_NOT_LIKE = 0;
    @Ignore
    public static final int FOOD_LIKE = 1;

    public Food(String foodName, Float food1serving, Float foodKcal, Float foodCarbohydrates, Float foodProtein, Float foodFat, String food_company, Integer foodLike) {

        this.foodName = foodName;
        this.food1serving = food1serving;
        this.foodKcal = foodKcal;
        this.foodCarbohydrates = foodCarbohydrates;
        this.foodProtein = foodProtein;
        this.foodFat = foodFat;
        this.food_company = food_company;
        this.foodLike = foodLike;
    }
    @Ignore
    public Food(@Nullable Long foodIndex,String foodName, Float food1serving, Float foodKcal, Float foodCarbohydrates, Float foodProtein, Float foodFat, String food_company, Integer foodLike) {
        if(foodIndex!=null)
            this.foodIndex=foodIndex;
        this.foodName = foodName;
        this.food1serving = food1serving;
        this.foodKcal = foodKcal;
        this.foodCarbohydrates = foodCarbohydrates;
        this.foodProtein = foodProtein;
        this.foodFat = foodFat;
        this.food_company = food_company;
        this.foodLike = foodLike;
    }
    //생성자 오버로딩
    @Ignore
    public Food(String foodName, float food1serving, float foodKcal, float foodCarbohydrates, float foodProtein, float foodFat, String food_company) {
        this.foodName = foodName;
        this.food1serving = food1serving;
        this.foodKcal = foodKcal;
        this.foodCarbohydrates = foodCarbohydrates;
        this.foodProtein = foodProtein;
        this.foodFat = foodFat;
        this.food_company = food_company;

        foodLike = FOOD_NOT_LIKE;
    }

    // getter methods...
    public Long getFoodIndex() { return foodIndex; }

    public String getFoodName() { return foodName; }
    public Float getFood1serving() { return food1serving; }
    public Float getFoodKcal() { return foodKcal; }
    public Float getFoodCarbohydrates() { return foodCarbohydrates; }
    public Float getFoodProtein() { return foodProtein; }
    public Float getFoodFat() { return foodFat; }
    public String getFood_company() { return food_company; }
    public Integer getFoodLike() { return foodLike; }

    // 인덱스를 수정하는 setter method
    public void setFoodIndex(Long foodIndex) {this.foodIndex = foodIndex;}

    public void setFoodLike(Integer foodLike) {
        this.foodLike = foodLike;
    }


    /**
     * Note:
     *  변수 들의 값을 초기 설정 하지 않으면  equals 사용시 에러가 난다.
     *  NullPointException -> var 들의 초기값 할당 해주어 해결
     */
    // equals method
    public boolean equals(Object o) {
        float EPSILON = 0.1f;
        if (o == this) return true;
        if (!(o instanceof Food)) return false;
        Food other = (Food) o;
        if (!this.foodName.equals(other.foodName)) return false;
        if (Math.abs(this.food1serving - other.food1serving) > EPSILON) return false;
        if (Math.abs(this.foodKcal - other.foodKcal) > EPSILON) return false;
        if (Math.abs(this.foodCarbohydrates - other.foodCarbohydrates) > EPSILON) return false;
        if (Math.abs(this.foodProtein - other.foodProtein) > EPSILON) return false;
        if (Math.abs(this.foodFat - other.foodFat) > EPSILON) return false;
        if (!this.food_company.equals(other.food_company)) return false;
        if (this.foodLike != other.foodLike) return false;
        return true;
    }

}