package com.burgerdelivery.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.dagger.component.ApplicationComponent;

public abstract class BaseActivity<TView> extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationComponent applicationComponent = BurgerDeliveryApplication.getApplicationComponent(this);
        onInjectDependencies(applicationComponent);

        presenterImplementation().setView(viewImplementation());
    }

    protected abstract void onInjectDependencies(ApplicationComponent applicationComponent);

    protected abstract BasePresenter<TView> presenterImplementation();
    protected abstract TView viewImplementation();
}
