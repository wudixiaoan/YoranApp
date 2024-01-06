package com.ph.common.module.http.util;

import android.util.Log;

import com.ph.common.module.http.api.DownloadService;
import com.ph.common.module.http.control.BaseDownloadObserver;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class DownloadUtils {
    private static final String TAG = "DownloadAPI";
    private static final int DEFAULT_TIMEOUT = 60;
    public Retrofit retrofit;

    public DownloadUtils() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://test.zigoomo.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public DownloadUtils(String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public DownloadUtils(String baseUrl, DownloadProgressListener listener) {
        DownloadInterceptor interceptor = new DownloadInterceptor(listener);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public void downloadFile(String url, final String destDir, final String fileName, final FileDownloadCallback fileDownLoadObserver) {
        Log.d(TAG, "downloadFile: " + url);
        retrofit.create(DownloadService.class)
                .download(url)
                .subscribeOn(Schedulers.io())//请求网络 在调度者的io线程
                .observeOn(Schedulers.io()) //指定线程保存文件
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        return fileDownLoadObserver.saveFile(responseBody.byteStream(), destDir, fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseDownloadObserver<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    protected void onDownloadSuccess(File file) {
                        //踩坑，接口回调传递file堆栈溢出报错,修改为传递文件路径
                        fileDownLoadObserver.onDownLoadSuccess(file.getAbsolutePath());
                    }

                    @Override
                    protected void onDownloadError(Throwable e) {
                        fileDownLoadObserver.onDownLoadFail(e);
                    }
                });
    }
}
