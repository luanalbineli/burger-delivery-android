package com.burgerdelivery.dagger.component;

import com.burgerdelivery.burgerdetail.BurgerDetailFragment;
import com.burgerdelivery.dagger.PerFragment;
import com.burgerdelivery.burgerlist.BurgerListFragment;
import com.burgerdelivery.orderitemlist.OrderItemListActivity;
import com.burgerdelivery.widget.OrderShortcutWidgetProvider;
import com.burgerdelivery.widget.RecipeIngredientListViewService;

import dagger.Component;

@PerFragment
@Component(dependencies = ApplicationComponent.class)
public interface InjectorComponent {
    void inject(BurgerListFragment hamburgerListFragment);

    void inject(BurgerDetailFragment burgerDetailFragment);

    void inject(OrderItemListActivity activity);

    void inject(OrderShortcutWidgetProvider recipeShortcutWidgetProvider);

    void inject(RecipeIngredientListViewService.WidgetRemoteViewsFactory widgetRemoteViewsFactory);
}
