package com.ph.common.module.http.control;


import com.ph.common.log.XLog;

import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {
    private static final String TAG = "HttpObserver";

    public abstract void onSuccess(T result);

    public abstract void onFailure(Throwable e, String errorMsg);

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(@Nullable T t) {
        try {
            if (t == null) {
                onFailure(new Exception(), "");
            } else {
                onSuccess(t);
            }
        } catch (Exception e) {
            XLog.i(TAG, "onNext", e.getMessage());
            onFailure(e, "系统报错");
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        XLog.i(TAG, "onNext", e.getMessage());
        onFailure(e, e.getMessage());
    }
}

