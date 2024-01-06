package com.ph.common.module.http.util;


import com.ph.common.module.http.dto.DownloadResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class DownloadInterceptor implements Interceptor {

    private DownloadProgressListener listener;

    public DownloadInterceptor() {
    }

    public DownloadInterceptor(DownloadProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new DownloadResponseBody(originalResponse.body(), listener))
                .build();
    }
}
