package com.yoran.app.present.main;

import com.yoran.app.model.DataClient;
import com.yoran.app.present.RxBasePresenter;

import javax.inject.Inject;

public class MainPresenter extends RxBasePresenter<MainContract.MainView> implements MainContract.MainActivityPresenter {

    private DataClient mDataClient;

    @Inject
    public MainPresenter(DataClient dataClient) {
        super(dataClient);
        this.mDataClient = dataClient;
    }
}
