package com.burgerdelivery.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.dagger.component.ApplicationComponent;

public abstract class BaseFragment<TView> extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationComponent applicationComponent = BurgerDeliveryApplication.getApplicationComponent(getActivity());
        onInjectDependencies(applicationComponent);

        presenterImplementation().setView(viewImplementation());
    }

    protected abstract void onInjectDependencies(ApplicationComponent applicationComponent);

    protected abstract BasePresenter<TView> presenterImplementation();
    protected abstract TView viewImplementation();
}
