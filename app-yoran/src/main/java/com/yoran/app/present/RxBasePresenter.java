package com.yoran.app.present;

import com.ph.common.base.BaseView;
import com.ph.common.base.presenter.AbstractBasePresenter;
import com.yoran.app.model.DataClient;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxBasePresenter<T extends BaseView> implements AbstractBasePresenter<T> {

    protected T mView;
    /**
     * 一个disposable的容器，可以容纳多个disposable 防止订阅之后没有取消订阅的内存泄漏
     */
    private CompositeDisposable compositeDisposable;
    private DataClient mDataClient;


    public RxBasePresenter(DataClient dataClient) {
        this.mDataClient = dataClient;
    }

    // 将订阅时间 event 加入到 disposable的容器中
    protected void addEventSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public DataClient getDataClient() {
        return mDataClient;
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
        if (mView != null) {
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    @Override
    public void addRxBindingSubscribe(Disposable disposable) {
        addEventSubscribe(disposable);
    }
}

