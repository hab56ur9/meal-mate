package com.anonymous.mealmate.view.adapter;

import android.widget.RadioGroup;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.anonymous.mealmate.constants.UserHashMap;

import java.util.Map;
import java.util.Objects;

public class UserBindingAdapters {

    // 값으로 키를 찾는 보조 메서드
    private static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    // 성별 설정 : 체크 시켜주는 거
    @BindingAdapter("app:gender")  // 바인딩 어댑터 메소드 선언. xml 파일에서 사용되는 커스텀 속성 정의.
    public static void setGender(RadioGroup view, int gender) {
        Integer id = getKeyByValue(UserHashMap.GenderMap, gender);
        if(id!=null){view.check(id);}
    }

    // 성별 가져오기 : 체크 한 값 가져오는 거
    @InverseBindingAdapter(attribute = "app:gender")  // 양방향 바인딩을 위한 어댑터 메소드 선언. 특정 속성의 값이 변경될 때마다 메소드 호출. 최신 값 반환
    public static int getGender(RadioGroup view) {
        int id = view.getCheckedRadioButtonId();
        return UserHashMap.GenderMap.getOrDefault(id, -1);
    }

    // 목적 설정
    @BindingAdapter("app:purpose")
    public static void setPurpose(RadioGroup view, int purpose) {
        Integer id = getKeyByValue(UserHashMap.PURPOSE_MAP, purpose);
        if(id!=null){view.check(id);}
    }

    // 목적 가져오기
    @InverseBindingAdapter(attribute = "app:purpose")
    public static int getPurpose(RadioGroup view) {
        int id = view.getCheckedRadioButtonId();
        return UserHashMap.PURPOSE_MAP.getOrDefault(id, -1);
    }

    // 활동 수준 설정
    @BindingAdapter("app:activityLevel")
    public static void setActivityLevel(RadioGroup view, int activityLevel) {
        Integer id = getKeyByValue(UserHashMap.ACTIVITY_MAP, activityLevel);
        if(id!=null){view.check(id);}
    }

    // 활동 수준 가져오기
    @InverseBindingAdapter(attribute = "app:activityLevel")
    public static int getActivityLevel(RadioGroup radioGroup) {
        int id = radioGroup.getCheckedRadioButtonId();
        return UserHashMap.ACTIVITY_MAP.getOrDefault(id, -1);
    }

    // 두 개 이상의 속성을 바인딩 어댑터에 연결할 때 사용.
    @BindingAdapter(value = {"android:onCheckedChanged", "genderAttrChanged"}, requireAll = false)
    public static void setListeners(RadioGroup radioGroup, final RadioGroup.OnCheckedChangeListener listener, final InverseBindingListener attrChange) {
        if (attrChange == null) {  // attrChange가 null이면 listener를 바로 설정
            radioGroup.setOnCheckedChangeListener(listener);
        } else {
            radioGroup.setOnCheckedChangeListener(((group, checkedId) -> {
                if (listener != null) { // listener가 null이 아니면 listener.onCheckedChanged 호출
                    listener.onCheckedChanged(group, checkedId);
                }
                attrChange.onChange();
            }));
        }
    }

    //purpose
    @BindingAdapter(value = {"android:onCheckedChanged", "purposeAttrChanged"}, requireAll = false)
    public static void setListeners2(RadioGroup radioGroup, final RadioGroup.OnCheckedChangeListener listener, final InverseBindingListener attrChange) {
        if (attrChange == null) {  // attrChange가 null이면 listener를 바로 설정
            radioGroup.setOnCheckedChangeListener(listener);
        } else {
            radioGroup.setOnCheckedChangeListener(((group, checkedId) -> {
                if (listener != null) { // listener가 null이 아니면 listener.onCheckedChanged 호출
                    listener.onCheckedChanged(group, checkedId);
                }
                attrChange.onChange();
            }));
        }
    }

    @BindingAdapter(value = {"android:onCheckedChanged", "activityLevelAttrChanged"}, requireAll = false)
    public static void setListeners3(RadioGroup radioGroup, final RadioGroup.OnCheckedChangeListener listener, final InverseBindingListener attrChange) {
        if (attrChange == null) {  // attrChange가 null이면 listener를 바로 설정
            radioGroup.setOnCheckedChangeListener(listener);
        } else {
            radioGroup.setOnCheckedChangeListener(((group, checkedId) -> {
                if (listener != null) { // listener가 null이 아니면 listener.onCheckedChanged 호출
                    listener.onCheckedChanged(group, checkedId);
                }
                attrChange.onChange();
            }));
        }
    }
}