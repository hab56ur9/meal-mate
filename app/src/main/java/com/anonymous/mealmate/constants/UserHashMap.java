package com.anonymous.mealmate.constants;

import com.anonymous.mealmate.R;
import com.anonymous.mealmate.model.entity.User;

import java.util.HashMap;

public class UserHashMap {

    public static final HashMap<Integer, Integer> GenderMap = new HashMap<>();

    //성별 해쉬맵
    static {
        GenderMap.put(R.id.rb_survey_male, User.MALE);
        GenderMap.put(R.id.rb_survey_female, User.FEMALE);
    }

    public static final HashMap<Integer, Integer> PURPOSE_MAP = new HashMap<>();

    //목적 해쉬맵
    static {
        PURPOSE_MAP.put(R.id.rb_survey_diet, User.DIET);
        PURPOSE_MAP.put(R.id.btn_purpose_diet, User.DIET);
        PURPOSE_MAP.put(R.id.rb_survey_bulk, User.BULK);
        PURPOSE_MAP.put(R.id.btn_purpose_bulkup, User.BULK);
        PURPOSE_MAP.put(R.id.rb_survey_maintain, User.MAINTAIN);
        PURPOSE_MAP.put(R.id.btn_purpose_maintenance, User.MAINTAIN);
    }

    public static final HashMap<Integer, Integer> ACTIVITY_MAP = new HashMap<>();

    //활동량 해쉬맵
    static {
        ACTIVITY_MAP.put(R.id.rb_survey_static, User.SEDENTARY);
        ACTIVITY_MAP.put(R.id.rb_survey_light, User.LIGHT);
        ACTIVITY_MAP.put(R.id.rb_survey_normal, User.MODERATE);
        ACTIVITY_MAP.put(R.id.rb_survey_active, User.ACTIVE);
        ACTIVITY_MAP.put(R.id.rb_survey_very_active, User.VERYACTIVE);
    }
}
