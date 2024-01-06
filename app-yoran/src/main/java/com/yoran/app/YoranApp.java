package com.yoran.app;

import android.content.Context;

import com.ph.common.base.BaseApplication;
import com.ph.common.log.XLog;
import com.yoran.app.di.component.DaggerAppComponent;
import com.yoran.app.model.db.DaoMaster;
import com.yoran.app.model.db.DaoSession;
import com.yoran.app.model.db.ReleaseOpenHelper;

import org.greenrobot.greendao.database.Database;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class YoranApp extends BaseApplication {
    private static YoranApp mInstance;
    public static final String DB_NAME = "yoran.db";
    private DaoSession mDaoSession;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.create();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mInstance = this;
        initGreenDao();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    //初始化GreenDao 数据库
    private void initGreenDao() {
        final ReleaseOpenHelper helper = new ReleaseOpenHelper(this, DB_NAME);
        final Database db = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    public static synchronized YoranApp getInstance() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        XLog.closeXLog();
    }
}
