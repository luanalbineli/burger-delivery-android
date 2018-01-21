package com.burgerdelivery.dagger.component;

import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.dagger.modules.ApplicationModule;
import com.burgerdelivery.repository.loader.BurgerListLoader;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Retrofit providesRetrofit();

    BurgerDeliveryApplication providesApplication();

    BurgerListLoader providesHamburgerListLoader();
}
