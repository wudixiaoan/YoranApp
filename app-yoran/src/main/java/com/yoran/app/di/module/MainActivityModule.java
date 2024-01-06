package com.yoran.app.di.module;

import com.ph.common.di.scope.ActivityScope;
import com.yoran.app.present.main.MainContract;
import com.yoran.app.present.main.MainPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MainActivityModule {
    @Binds
    @ActivityScope
    abstract MainContract.MainActivityPresenter bindPresenter(MainPresenter presenter);
}
