package com.anonymous.mealmate.feature;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

//트리거 클래스
//뷰의 이동을 제어 한다. activity, fragment 에서 데이터 핸들링을 하지 않기 때문에 단순 인텐트로 어디서 든지 화면 전환 가능 하도록 설계 할 수있다.
// ->  MVVM 의 최대장점 -> 복잡한 이벤트 구현이 쉽다.
//liveData 인 stateSignalLive 데이터를 액티비티에서 observe 한다음 상수값으로 선언된 signal에따라 switch 문으로 기능을 작동하도록 설계

@Singleton
public class ControlViewState {

    private MutableLiveData<Integer> stateSignalLiveData = new MutableLiveData<>(NONE);
    public LiveData<Integer> getStateSignalLiveData() {
        return stateSignalLiveData;
    }

    @Inject
    public ControlViewState(){};
    public void deactivateSignal(){
        stateSignalLiveData.setValue(NONE);
    }

    // 메소드 이름으로 동작 구분을 위해 같은 기능을 하는 메소드 여러개 생성
    public void activeIntentSignal(Integer signal){
        //Todo if문 범위값 지정 해서 Intent signal 인지 확인
        //active observer signal
        stateSignalLiveData.setValue(signal);

        Log.d(getClass().getName(), "activeIntentSignal: "+signal);
    }

    public void activeDialogSignal(Integer signal){
        //Todo if문 범위 값 지정 해서 Dialog 만의 신호 범위 인지 확인
        stateSignalLiveData.setValue(signal);

        Log.d(getClass().getName(), "activeDialogSignal: "+signal);
    }

    public void activeActivityLifeCycleSignal(Integer signal){
        //Todo if문 범위 값 지정 해서 Lifecycle 만의 신호 범위 인지 확인
        stateSignalLiveData.setValue(signal);

        Log.d(getClass().getName(), "activeActivityLifeCycleSignal: "+signal);
    }

    public void activeAdapterDataChangedSignal(Integer signal){
        // 비동기 작업중인 스레드 에서 호출 되므로 post
        if(stateSignalLiveData.getValue() == ADAPTER_MEAL_CHANGED)
            ++signal;
        stateSignalLiveData.postValue(signal);
        //옵저버 작동후 초기화
        Log.d(getClass().getName(), "activeAdapterDataChangedSignal: "+signal);
    }

    public static final int NONE = -1;

    // Activity LifeCycle Signal
    public static final int ACTIVITY_FINISH_SETMEAL =  1;
    public static final int ACTIVITY_FINISH_FOOD= 3;
    public static final int ACTIVITY_RESTART = 2;

    //Intent Signal
    public static final int INTENT_MAIN_TO_SETMEAL = 11;
    public static final int INTENT_MAIN_TO_USERUPDATEDATA = 12;
    public static final int INTENT_SETMEAL_TO_FOOD = 13;
    public static final int INTENT_SPLASH_TO_MAIN = 14;
    public static final int INTENT_SPALSH_TO_USERUPDATEDATA = 15;
    public static final int INTENT_USERUPDATEDATA_TO_MAIN = 16;


    //Dialog Signal
    public static final int DIALOG_DISMISS = 20;
    public static final int DIALOG_FOOD_DATASET = 21;
    public static final int DIALOG_MEAL_SAVE_PRESET = 23;
    public static final int DIALOG_MEAL_LOAD_PRESET = 25;
    public static final int DIALOG_MEALFOOD_DATASET  = 26;
    public static final int DIALOG_WEIGHT_CONFIG =  27;
    public static final int DIALOG_PURPOSE_CONFIG = 28;



    // adapter data updated signal

    private MutableLiveData<Integer> adapterDataChangedSignal  = new MutableLiveData<>(0);
    public LiveData<Integer> getAdapterSignal(){
        return adapterDataChangedSignal;
    }
    public void activeAdapterDataChangedSignal(){
        adapterDataChangedSignal.postValue(adapterDataChangedSignal.getValue()==0?1:0);
    }
    public static final int ADAPTER_MEAL_CHANGED = 31; // 32
    public static final int ADAPTER_MEALITEM_CHANGED = 33; // 34
    public static final int ADAPTER_MEALSUBITEM_CHANGED = 35; // 36 도 같이 사용
    public static final int ADAPTER_FOOD_CHANGED = 37; // 38 사용

}


