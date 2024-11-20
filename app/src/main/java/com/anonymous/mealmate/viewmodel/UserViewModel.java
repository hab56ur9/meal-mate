package com.anonymous.mealmate.viewmodel;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.model.entity.User;
import com.anonymous.mealmate.model.entity.Weekly;
import com.anonymous.mealmate.model.repository.JoinRepository;
import com.anonymous.mealmate.model.repository.UserRepository;
import com.anonymous.mealmate.service.ConversionService;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private final UserRepository repository;
    private final JoinRepository joinRepository;
    private final ControlViewState controlViewState;
    private final ConversionService conversionService;

    private  MutableLiveData<Float> bmr = new MutableLiveData<>();
    private LiveData<User> userLiveData;
    public MutableLiveData<Integer> userDataExists = new MutableLiveData<>();
    private  MutableLiveData<Float> height = new MutableLiveData<>();
    private  MutableLiveData<Float> weight = new MutableLiveData<>();
    private  MutableLiveData<Integer> age = new MutableLiveData<>();
    private  MutableLiveData<Float> targetWeight = new MutableLiveData<>();
    private  MutableLiveData<List<Weekly>> weeklyTotalCalories = new MutableLiveData<>();

    @Inject
    public UserViewModel(UserRepository userRepository, ControlViewState controlViewState, JoinRepository joinRepository, ConversionService conversionService) {
        this.repository = userRepository;
        this.controlViewState = controlViewState;
        this.joinRepository = joinRepository;
        this.conversionService = conversionService;

        userLiveData = repository.getUser();

        getUser().observeForever(user -> {
            if (user == null)
                controlViewState.activeIntentSignal(ControlViewState.INTENT_SPALSH_TO_USERUPDATEDATA);
            else
                controlViewState.activeIntentSignal(ControlViewState.INTENT_SPLASH_TO_MAIN);
        });
    }

    public void updateHeightWeightAge(User user) {

        Log.e("UserViewModel", "updateHeightWeightAge: " + height.getValue() + " " + weight.getValue() + " " + age.getValue() + " " + targetWeight.getValue() + " " + user.toString());

        //set해줄 때 null이면 기존의 user값 그대로 들어가게 하기
        user.setHeight(height.getValue() == null ? user.getHeight() : height.getValue());
        user.setTargetWeight(targetWeight.getValue() == null ? user.getTargetWeight() : targetWeight.getValue());
        user.setWeight(weight.getValue() == null ? user.getWeight() : weight.getValue());
        user.setAge(age.getValue() == null ? user.getAge() : age.getValue());
    }

    public void insert(User user) {
        repository.insertUser(user);
    }

    public void update(User user) {
        repository.updateUser(user);
    }

    public void delete(User user) {
        repository.deleteUser(user);
    }

    public void checkUserData() {
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void onInsertUserData(User user) {
        Log.d("UserViewModel", "Before insert : " + (user != null ? user.toString() : "null"));

        updateHeightWeightAge(user);

        Log.e("UserViewModel", "onInsertUserData: " + user.toString());
        controlViewState.activeIntentSignal(ControlViewState.INTENT_USERUPDATEDATA_TO_MAIN);

        repository.insertUser(user);
        // 다시 상태 확인 로그
        Log.e("UserViewModel", "After insert: " + (user != null ? user.toString() : "null"));
    }

  /*  public void onUpdateUserData(User user) {
        Log.d("UserViewModel", "Before update : " + (user != null ? user.toString() : "null"));

        updateHeightWeightAge(user);

        Log.e("UserViewModel", "onUpdateUserData: " + user.toString());
        controlViewState.activeIntentSignal(ControlViewState.INTENT_USERUPDATEDATA_TO_MAIN);

        repository.updateUser(user);
        // 다시 상태 확인 로그
        Log.e("UserViewModel", "After update: " + (user != null ? user.toString() : "null"));
    }*/

    public void setChartData() {
        // 라이브데이터에 저장된 유저의 BMR값을 가져온다.
        conversionService.setBackgroundTask(() -> {
            //getBmr();
            getWeeklyTotalCalories();
        }).runInTransaction();
    }

    public void getWeeklyTotalCalories() {
        conversionService.setBackgroundTask(() -> {
            // 라이브데이터에 저장된 유저의 BMR값을 가져온다.
            weeklyTotalCalories.postValue(joinRepository.getWeeklyTotalCalories());
            Log.e("UserViewModel", "getWeeklyTotalCalories: 백그라운드 실행 " + weeklyTotalCalories.getValue());
        }).runInTransaction();

        Log.e("UserViewModel", "getWeeklyTotalCalories: 백그라운드 종료 " + weeklyTotalCalories.getValue());
    }

    public LiveData<List<Weekly>> getWeeklyTotalCaloriesLiveData() {
        return weeklyTotalCalories;
    }

    public void setTargetWeightString(String targetString) {
        try {
            targetWeight.setValue(Float.parseFloat(targetString));
        } catch (NumberFormatException e) {
            targetWeight.setValue(null);
        }
    }

    public void setHeightString(String heightString) {
        try {
            //user에 height값을 Float형으로 저장해야 돼.
            height.setValue(Float.parseFloat(heightString));
        } catch (NumberFormatException e) {
            height.setValue(null);
        }
    }

    public void setWeightString(String weightString) {
        try {
            weight.setValue(Float.parseFloat(weightString));
        } catch (NumberFormatException e) {
            weight.setValue(null);
        }
    }

    public void setAgeString(String ageString) {
        try {
            age.setValue(Integer.parseInt(ageString));
        } catch (NumberFormatException e) {
            age.setValue(null);
        }
    }

    public String getHeightString() {
        if (height.getValue() == null || height.getValue().equals("")) {
            return "";
        } else {
            return String.valueOf(height.getValue());
        }
    }

    public String getWeightString() {
        if (weight.getValue() == null || weight.getValue().equals("")) {
            return "";
        } else {
            return String.valueOf(weight.getValue());
        }
    }

    public String getAgeString() {
        if (age.getValue() == null || age.getValue().equals("")) {
            return "";
        } else {
            return String.valueOf(age.getValue());
        }
    }

    public String getTargetWeightString() {
        if(targetWeight.getValue() == null || targetWeight.getValue().equals("")) {
            return "";
        } else {
            return String.valueOf(targetWeight.getValue());
        }
    }
}