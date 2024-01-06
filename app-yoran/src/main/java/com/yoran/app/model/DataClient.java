package com.yoran.app.model;


import com.yoran.app.model.db.DbHelper;
import com.yoran.app.model.http.helper.IHttpHelper;
import com.yoran.app.model.sp.SharedPreferenceHelper;

public class DataClient implements IHttpHelper, SharedPreferenceHelper, DbHelper {

    private IHttpHelper mIHttpHelper;
    private SharedPreferenceHelper mSharedPreferenceHelper;
    private DbHelper mDbHelper;

    public DataClient(IHttpHelper mIHttpHelper,
                      SharedPreferenceHelper mSharedPreferenceHelper,
                      DbHelper mDbHelper) {
        this.mIHttpHelper = mIHttpHelper;
        this.mSharedPreferenceHelper = mSharedPreferenceHelper;
        this.mDbHelper = mDbHelper;
    }
}
