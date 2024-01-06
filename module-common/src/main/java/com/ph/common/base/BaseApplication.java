package com.ph.common.base;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ph.common.BuildConfig;

import dagger.android.DaggerApplication;

public abstract class BaseApplication extends DaggerApplication {

    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        initARouter();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mInstance = this;
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    /**
     * 初始化路由
     */
    private void initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();  // 打印日志
            ARouter.openDebug(); // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(mInstance);// 尽可能早，推荐在Application中初始化
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        ActivityCollector.getInstance().finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.gc();
        System.exit(0);
    }
}
