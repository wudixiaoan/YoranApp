package com.ph.common.base;

import android.app.Activity;

import com.ph.common.log.XLog;

import java.util.HashMap;
import java.util.Map;

public class ActivityCollector {
    private static final String TAG = "ActivityCollector";
    private HashMap<String, Activity> mActivityHashMap = new HashMap<>();
    private static volatile ActivityCollector singleton;

    private String mCurrentActivityKey = null;

    private ActivityCollector() {
    }

    public static ActivityCollector getInstance() {
        if (singleton == null) {
            synchronized (ActivityCollector.class) {
                if (singleton == null) {
                    singleton = new ActivityCollector();
                }
            }
        }
        return singleton;
    }

    public Activity getCurrentActivity() {
        return mActivityHashMap.get(mCurrentActivityKey);
    }

    public void finishCurrentActivity() {
        Activity activity = mActivityHashMap.get(mCurrentActivityKey);
        if (activity == null) {
            return;
        }
        if (!activity.isFinishing()) {
            activity.finish();
        }
        mActivityHashMap.remove(mCurrentActivityKey);
    }

    public void addActivity(String key, Activity activity) {
        mCurrentActivityKey = key;
        mActivityHashMap.put(key, activity);
    }

    public void removeActivity(String key) {
        Activity activity = mActivityHashMap.remove(key);
        if (activity != null) {
            activity.finish();
        }
    }

    public void finishAll() {
        for (Map.Entry<String, Activity> entry : mActivityHashMap.entrySet()) {
            Activity activity = entry.getValue();
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        mActivityHashMap.clear();
    }
}
