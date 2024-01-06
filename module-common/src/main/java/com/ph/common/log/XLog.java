package com.ph.common.log;

import com.alibaba.fastjson.JSON;
import com.tencent.mars.BuildConfig;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

public class XLog {
    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");
    }

    public static void openXLog(String logPath, String logFile) {
        Xlog xlog = new Xlog();
        Log.setLogImp(xlog);
        Log.setConsoleLogOpen(false);
        Log.appenderOpen(Xlog.LEVEL_ALL, Xlog.AppednerModeAsync, "", logPath, logFile, 0);
    }

    public static void closeXLog() {
        Log.appenderClose();
    }

    public static void i(String tag, String msg) {
        android.util.Log.i(tag, msg);
        Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(tag, msg);
        Log.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        android.util.Log.d(tag, msg);
        Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        android.util.Log.v(tag, msg);
        Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        android.util.Log.w(tag, msg);
        Log.w(tag, msg);
    }

    public static void i(String tag, String function, String msg) {
        android.util.Log.i(tag, "[" + function + "] - [" + msg + "]");
        Log.i(tag, "[" + function + "] - [" + msg + "]");
    }

    public static void i(String tag, String function, Object object) {
        android.util.Log.i(tag, "[" + function + "] - " + JSON.toJSONString(object));
        Log.i(tag, "[" + function + "] - " + JSON.toJSONString(object));
    }

    public static void i(String tag, String function, String msg, Object object) {
        android.util.Log.i(tag, "[" + function + "] - [" + msg + "] - " + JSON.toJSONString(object));
        Log.i(tag, "[" + function + "] - [" + msg + "] - " + JSON.toJSONString(object));
    }

    public static void i(String tag, String function, String msg, String object) {
        android.util.Log.i(tag, "[" + function + "] - [" + msg + "] - " + object);
        Log.i(tag, "[" + function + "] - [" + msg + "] - " + object);
    }

    public static void i(String tag, String function, String boxId, String msg, Object object) {
        android.util.Log.i(tag, "[" + function + "] - [" + boxId + "] - [" + msg + "] - " + JSON.toJSONString(object));
        Log.i(tag, "[" + function + "] - [" + boxId + "] - [" + msg + "] - " + JSON.toJSONString(object));
    }

    public static void e(String tag, String function, String msg) {
        android.util.Log.e(tag, "[" + function + "] - " + msg);
        Log.e(tag, "[" + function + "] - " + msg);
    }

    public static void d(String tag, String function, String msg) {
        android.util.Log.d(tag, "[" + function + "] - [" + msg + "]");
        if (BuildConfig.DEBUG) Log.d(tag, "[" + function + "] - [" + msg + "]");
    }

    public static void d(String tag, String function, Object object) {
        android.util.Log.d(tag, "[" + function + "] - " + JSON.toJSONString(object));
        if (BuildConfig.DEBUG) Log.d(tag, "[" + function + "] - " + JSON.toJSONString(object));
    }

    public static void d(String tag, String function, String msg, Object object) {
        android.util.Log.d(tag, "[" + function + "] - [" + msg + "] - " + JSON.toJSONString(object));
        if (BuildConfig.DEBUG)
            Log.d(tag, "[" + function + "] - [" + msg + "] - " + JSON.toJSONString(object));
    }

    public static void d(String tag, String function, String msg, String object) {
        android.util.Log.d(tag, "[" + function + "] - [" + msg + "] - " + object);
        if (BuildConfig.DEBUG) Log.d(tag, "[" + function + "] - [" + msg + "] - " + object);
    }

    public static void d(String tag, String function, String boxId, String msg, Object object) {
        android.util.Log.d(tag, "[" + function + "] - [" + boxId + "] - [" + msg + "] - " + JSON.toJSONString(object));
        if (BuildConfig.DEBUG)
            Log.d(tag, "[" + function + "] - [" + boxId + "] - [" + msg + "] - " + JSON.toJSONString(object));
    }

    public static void v(String tag, String function, String msg) {
        android.util.Log.v(tag, "[" + function + "] - " + msg);
        Log.v(tag, "[" + function + "] - " + msg);
    }

    public static void w(String tag, String function, String msg) {
        android.util.Log.w(tag, "[" + function + "] - " + msg);
        Log.w(tag, "[" + function + "] - " + msg);
    }
}
