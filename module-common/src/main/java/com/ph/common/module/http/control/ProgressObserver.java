package com.ph.common.module.http.control;

import android.content.Context;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import io.reactivex.disposables.Disposable;

public abstract class ProgressObserver<T> extends BaseObserver<T> {

    private QMUITipDialog progressDialog;
    private Context mContext;
    private String mLoadingText;

    public ProgressObserver(Context context) {
        this(context, null);
    }

    public ProgressObserver(Context context, String loadingText) {
        this.mContext = context;
        this.mLoadingText = loadingText;
    }

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
    public void onComplete() {
        super.onComplete();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}