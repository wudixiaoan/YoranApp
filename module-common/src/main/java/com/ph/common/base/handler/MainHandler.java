package com.ph.common.base.handler;

import android.os.Handler;
import android.os.Looper;

public class MainHandler extends Handler {

    private static volatile MainHandler singleton;

    private MainHandler() {
        super(Looper.getMainLooper());
    }

    public static MainHandler getInstance() {
        if (singleton == null) {
            synchronized (MainHandler.class) {
                if (singleton == null) {
                    singleton = new MainHandler();
                }
            }
        }
        return singleton;
    }
}