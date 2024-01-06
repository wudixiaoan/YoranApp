package com.yoran.app.ui.main;

import com.ph.common.base.activity.BaseActivity;
import com.yoran.app.present.main.MainContract;
import com.yoran.app.present.main.MainPresenter;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.MainView {
    @Override
    public void showNormal() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showErrorMsg(String errorMsg) {

    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected int getLayout() {
        return 0;
    }
}
