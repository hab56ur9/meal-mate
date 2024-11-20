package com.anonymous.mealmate.model.database;


import androidx.room.Database;

import androidx.room.RoomDatabase;

import com.anonymous.mealmate.model.dao.FoodDao;
import com.anonymous.mealmate.model.dao.JoinDao;
import com.anonymous.mealmate.model.dao.MealDao;
import com.anonymous.mealmate.model.dao.MealFoodDao;
import com.anonymous.mealmate.model.dao.UserDao;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.model.entity.MealFood;
import com.anonymous.mealmate.model.entity.User;

import javax.inject.Singleton;

// version = 1 : 처음 생성할 때 버전 1로 생성. 이후에 업데이트할 때마다 버전을 올려줘야 함.
@Database(entities = {Meal.class, Food.class, User.class, MealFood.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "test_database1";

    public abstract MealDao mealDao();
    public abstract FoodDao foodDao();
    public abstract UserDao userDao();
    public abstract MealFoodDao mealFoodDao();
    public abstract JoinDao joinDao();

}
    /*private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // MealFood 테이블을 추가합니다.
            database.execSQL("CREATE TABLE mealFood (mealIndex INTEGER, foodIndex INTEGER, mealFoodAmount INTEGER, checked INTEGER, " +
                    "PRIMARY KEY(mealIndex, foodIndex)," +
                    "FOREIGN KEY(mealIndex) REFERENCES meal(mealIndex)," +
                    "FOREIGN KEY(foodIndex) REFERENCES food(foodIndex))");

            // Meal 테이블에서 더 이상 사용하지 않는 칼럼을 제거합니다.
            database.execSQL("CREATE TABLE new_meal (mealIndex INTEGER PRIMARY KEY NOT NULL, mealDate TEXT, mealCnt INTEGER)");
            database.execSQL("INSERT INTO new_meal (mealIndex, mealDate, mealCnt) SELECT mealIndex, mealDate, mealCnt FROM meal");
            database.execSQL("DROP TABLE meal");
            database.execSQL("ALTER TABLE new_meal RENAME TO meal");
        }
    };*/
