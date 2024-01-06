package com.yoran.app.model.sp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ph.common.base.BaseApplication;

import javax.inject.Inject;

public class SharedPreferenceHelperImpl implements SharedPreferenceHelper {

    private final SharedPreferences mSharedPreferences;

    @Inject
    public SharedPreferenceHelperImpl() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
    }

}