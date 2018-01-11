package com.burgerdelivery;

import android.app.Application;
import android.content.Context;

import com.burgerdelivery.dagger.component.ApplicationComponent;
import com.burgerdelivery.dagger.component.DaggerApplicationComponent;
import com.burgerdelivery.dagger.modules.ApplicationModule;

import timber.log.Timber;

public class BurgerDeliveryApplication extends Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent getApplicationComponent(Context context) {
        return ((BurgerDeliveryApplication) context.getApplicationContext()).mApplicationComponent;
    }
}
