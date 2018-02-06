package com.burgerdelivery;

import android.app.Application;
import android.content.Context;

import com.burgerdelivery.dagger.component.ApplicationComponent;
import com.burgerdelivery.dagger.component.DaggerApplicationComponent;
import com.burgerdelivery.dagger.modules.ApplicationModule;
import com.burgerdelivery.util.ReleaseTree;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;

import java.util.HashSet;

import timber.log.Timber;

public class BurgerDeliveryApplication extends Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }

        Stetho.initializeWithDefaults(this);

        FirebaseApp.initializeApp(this);
        Timber.d("Initialized Firebase");

        HashSet<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setRequestListeners(requestListeners)
                .build();
        Fresco.initialize(this, config);

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent getApplicationComponent(Context context) {
        return ((BurgerDeliveryApplication) context.getApplicationContext()).mApplicationComponent;
    }
}
