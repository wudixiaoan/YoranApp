package com.yoran.app.model.http.helper;


import com.ph.common.module.http.util.NetworkUtils;
import com.yoran.app.model.http.api.ApiService;

import javax.inject.Inject;

public class IHttpHelperImpl implements IHttpHelper {

    private static final String TAG = "IHttpHelper";
    public NetworkUtils mNetworkUtils;

    private ApiService getHttpApiServiceGson(String host) {
        return mNetworkUtils.getHttpApiService(ApiService.class, host, true);
    }

    private ApiService getHttpApiServiceNotGson(String host) {
        return mNetworkUtils.getHttpApiService(ApiService.class, host, false);
    }

    private ApiService getHttpsApiServiceGson(String host) {
        return mNetworkUtils.getHttpsApiService(ApiService.class, host, true);
    }

    private ApiService getHttpsApiServiceNotGson(String host) {
        return mNetworkUtils.getHttpsApiService(ApiService.class, host, false);
    }

    @Inject
    public IHttpHelperImpl(NetworkUtils networkUtils) {
        this.mNetworkUtils = networkUtils;
    }


}
