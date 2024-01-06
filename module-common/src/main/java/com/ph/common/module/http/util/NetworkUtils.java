package com.ph.common.module.http.util;

import android.util.Log;

import com.ph.common.BuildConfig;
import com.ph.common.log.XLog;
import com.ph.common.module.http.interceptor.HttpLogInterceptor;
import com.ph.common.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkUtils {
    private final static String TGA = "NetworkUtils";
    private final static int DEFAULT_TIME_OUT = 30;
    private Retrofit.Builder mHttpRetrofitBuilder;
    private Retrofit.Builder mHttpsRetrofitBuilder;

    //双重效验锁实现单例
    /*private static volatile NetworkUtils mInstance;

    public static NetworkUtils getInstance() {
        if(mInstance==null){
            synchronized (NetworkUtils.class){
                if(mInstance==null){
                    mInstance=new NetworkUtils();
                }
            }
        }
        return mInstance;
    }*/
    @Inject
    public NetworkUtils() {
        mHttpRetrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient());

        mHttpsRetrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpsClient());
    }

    /**
     * 获取http网络请求对象
     *
     * @param service 对应的Api 接口类
     * @param url     url
     * @param useGson 是否使用 gson 解析
     * @param <T>
     * @return
     */
    public <T> T getHttpApiService(final Class<T> service, String url, boolean useGson) {
        if (useGson) {
            Log.i(TGA, "useGson url:" + url);
            return mHttpRetrofitBuilder.addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(url + "/")
                    .build()
                    .create(service);
        } else {
            Log.i(TGA, "not useGson url:" + url);
            return mHttpRetrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(url + "/")
                    .build()
                    .create(service);
        }
    }

    /**
     * 获取https网络请求对象
     *
     * @param service 对应的Api 接口类
     * @param url     url
     * @param useGson 是否使用 gson 解析
     * @param <T>
     * @return
     */
    public <T> T getHttpsApiService(final Class<T> service, String url, boolean useGson) {
        if (useGson) {
            Log.i(TGA, "useGson url:" + url);
            return mHttpsRetrofitBuilder.addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(url + "/")
                    .build()
                    .create(service);
        } else {
            Log.i(TGA, "not useGson url:" + url);
            return mHttpsRetrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(url + "/")
                    .build()
                    .create(service);
        }
    }

    public OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        //加入日志拦截器
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogging());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
        } else {
            HttpLogInterceptor logInterceptor = new HttpLogInterceptor(new HttpLogger());
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(logInterceptor);
        }
        //设置超时时间
        clientBuilder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                //错误重连
                .retryOnConnectionFailure(true);
        // cookie 操作处理
        return clientBuilder.build();
    }

    public OkHttpClient getOkHttpsClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLogInterceptor logInterceptor = new HttpLogInterceptor(new HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //加入日志拦截器
        clientBuilder.addInterceptor(logInterceptor);
        //设置超时时间
        clientBuilder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .sslSocketFactory(new SslContextFactory().getSslSocket().getSocketFactory())
                //错误重连
                .retryOnConnectionFailure(true);
        // cookie 操作处理
        return clientBuilder.build();
    }

    public class HttpLogger implements HttpLogInterceptor.Logger {
        @Override
        public void log(String message) {
            XLog.i("HttpLoggerInfo", message);
        }
    }

    public class HttpLogging implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            XLog.i("HttpLoggingInfo", message);
        }
    }

    public static String getSign(Map<String, Object> map,
                                 String customerSecretKey) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (entry.getValue().equals("") || entry.getValue().equals("null")) {
                continue;
            }
            list.add(entry.getKey() + "=" + entry.getValue() + "&");
        }

        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + customerSecretKey;
        result = SecurityUtils.md5(result).toUpperCase();
        return result;
    }
}

