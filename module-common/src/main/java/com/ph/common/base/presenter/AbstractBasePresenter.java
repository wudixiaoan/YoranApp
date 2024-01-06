package com.ph.common.base.presenter;

import com.ph.common.base.BaseView;

import io.reactivex.disposables.Disposable;

public interface AbstractBasePresenter<T extends BaseView> {

    /**
     * 绑定View
     *
     * @param view
     */
    void attachView(T view);

    /**
     * 解绑View
     */
    void detachView();

    /**
     * 添加 rxBing 订阅管理器
     *
     * @param disposable Disposable
     */
    void addRxBindingSubscribe(Disposable disposable);
}