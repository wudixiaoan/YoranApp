package com.yoran.app.model.db;

import com.yoran.app.YoranApp;

import javax.inject.Inject;

public class DbHelperImpl implements DbHelper {
    private static final String TAG = "DbHelper";
    private DaoSession daoSession;

    @Inject
    public DbHelperImpl() {
        daoSession = YoranApp.getInstance().getDaoSession();
    }

}
