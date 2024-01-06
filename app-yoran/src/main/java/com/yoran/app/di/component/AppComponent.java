package com.yoran.app.di.component;


import com.yoran.app.YoranApp;
import com.yoran.app.di.module.ActivityBindingModule;
import com.yoran.app.di.module.AppModule;
import com.yoran.app.di.module.FragmentBindingModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AppModule.class,
        ActivityBindingModule.class,
        FragmentBindingModule.class,
        AndroidSupportInjectionModule.class
})
public interface AppComponent extends AndroidInjector<YoranApp> {
}
