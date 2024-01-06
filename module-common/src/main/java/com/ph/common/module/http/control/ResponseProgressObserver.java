package com.ph.common.module.http.control;

import android.content.Context;

import com.ph.common.module.dto.CommonResponse;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class ResponseProgressObserver<T> implements Observer<CommonResponse<T>> {
    private QMUITipDialog progressDialog;
    private Context mContext;
    private String mLoadingText;

    public ResponseProgressObserver(Context context, String msg) {
        this.mContext = context;
        this.mLoadingText = msg;
    }

    public abstract void onSuccess(T result);

    public abstract void onFailure(String errorCode, String errorMsg);

    public abstract void onError(Throwable e, String errorMsg);

    @Override
    public void onSubscribe(Disposable d) {
        if (!d.isDisposed()) {
            QMUITipDialog.Builder builder = new QMUITipDialog.Builder(mContext);
            builder.setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING);
            builder.setTipWord(mLoadingText);
            progressDialog = builder.create();
            progressDialog.show();
        }
    }

    @Override
    public void onNext(CommonResponse<T> response) {
        if (response == null) {
            onError(new Exception(), "");
        } else if (response.getReturn_code() == 444) {
            onError(new Exception(), response.getErr_code_desc());
        } else if (response.getResult_code() == 444) {
            onFailure(response.getErr_code(), response.getErr_code_desc());
        } else {
            onSuccess(response.getResult_msg());
        }
    }

    @Override
    public void onComplete() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onError(Throwable e) {
        onError(e, e.getMessage());
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

