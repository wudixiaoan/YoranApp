package com.yoran.app.di.module;


import com.ph.lingmao.module.DataClient;
import com.ph.lingmao.module.db.DbHelperImpl;
import com.ph.lingmao.module.http.helper.IHttpHelperImpl;
import com.ph.lingmao.module.sp.SharedPreferenceHelperImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    public DataClient providerDataClient(IHttpHelperImpl iHttpHelper,
                                         SharedPreferenceHelperImpl sharedPreferenceHelper,
                                         DbHelperImpl dbHelper) {
        return new DataClient(iHttpHelper, sharedPreferenceHelper, dbHelper);
    }
}