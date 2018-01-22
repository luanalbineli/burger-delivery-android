package com.burgerdelivery.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.dagger.component.DaggerInjectorComponent;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.repository.BurgerRepository;

import javax.inject.Inject;

import timber.log.Timber;


public class OrderShortcutWidgetProvider extends AppWidgetProvider {
    @Inject
    BurgerRepository mBurgerRepository;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Timber.d("Updating the widgets. Count: " + appWidgetIds.length);
        DaggerInjectorComponent.builder()
                .applicationComponent(BurgerDeliveryApplication.getApplicationComponent(context))
                .build()
                .inject(this);

        for (int widgetId : appWidgetIds) {
            OrderModel orderModel = mBurgerRepository.getCurrentPendingOrder().blockingGet();
            Timber.d("Checking if there is a recipe model for it: " + orderModel);
            if (orderModel != null) { // TODO: Handle if the recipe was not found.
                RecipeWidgetManager.bindLayout(appWidgetManager, context, widgetId, orderModel);
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        DaggerInjectorComponent.builder()
                .applicationComponent(BurgerDeliveryApplication.getApplicationComponent(context))

                .build()
                .inject(this);

        for (int widgetId : appWidgetIds) {
            // TODO: REMOVE THE WIDGET INFO.
            //mBurgerRepository.removeRecipeIdByWidgetId(widgetId);
        }
    }
}
