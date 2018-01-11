package com.burgerdelivery.dagger.component;

import com.burgerdelivery.dagger.PerFragment;
import com.burgerdelivery.burgerlist.BurgerListFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = ApplicationComponent.class)
public interface InjectorComponent {
    void inject(BurgerListFragment hamburgerListFragment);
}
