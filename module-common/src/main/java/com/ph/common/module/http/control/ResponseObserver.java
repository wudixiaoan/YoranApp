package com.ph.common.module.http.control;

import com.ph.common.module.dto.CommonResponse;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class ResponseObserver<T> implements Observer<CommonResponse<T>> {
    private static final int SUCCESS_CODE = 666;
    private static final int FAIL_CODE = 444;
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private static final String FAIL_REASON = "fail_reason";
    private static final String RESPONSE_NULL = "返回信息为空";
    private static final String UNKNOWN_CN = "未知";
    private static final String UNKNOWN = "unknown";

    public abstract void onSuccess(T result);

    public abstract void onFailure(String errorCode, String errorMsg);

    public abstract void onError(Throwable e, String errorMsg);

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(CommonResponse<T> response) {
        if (response == null) {
            onError(new Exception(RESPONSE_NULL), RESPONSE_NULL);
        } else if (response.getReturn_code() == FAIL_CODE) {
            onError(new Exception(FAIL), response.getErr_code_desc());
        } else if (response.getResult_code() == FAIL_CODE) {
            onFailure(response.getErr_code(), response.getErr_code_desc());
        } else {
            onSuccess(response.getResult_msg());
        }
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        onError(e, e.getMessage());
    }
}
