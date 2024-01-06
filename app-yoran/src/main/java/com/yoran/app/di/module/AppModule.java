package com.yoran.app.di.module;



import com.yoran.app.model.DataClient;
import com.yoran.app.model.db.DbHelperImpl;
import com.yoran.app.model.http.helper.IHttpHelperImpl;
import com.yoran.app.model.sp.SharedPreferenceHelperImpl;

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