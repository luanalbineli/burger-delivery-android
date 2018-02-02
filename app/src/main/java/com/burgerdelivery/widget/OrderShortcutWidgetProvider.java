package com.burgerdelivery.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.dagger.component.DaggerInjectorComponent;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.repository.BurgerRepository;

import javax.inject.Inject;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
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

        mBurgerRepository.getLastOrder()
            .subscribe((orderModel) -> {
                for (int widgetId : appWidgetIds) {
                    RecipeWidgetManager.bindLayout(appWidgetManager, context, widgetId, orderModel);
                }
            }, throwable -> Timber.e(throwable, "An error occurred while tried to update the widgets. Number of widgets: " + appWidgetIds.length));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null && intent.hasExtra(WIDGET_IDS_KEY)) {
            int ids[] = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
        } else {
            super.onReceive(context, intent);
        }
    }

    private static final String WIDGET_IDS_KEY = "order_shortcut_widget_provider_ids";

    public static void sendBroadcastToUpdateTheWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, OrderShortcutWidgetProvider.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(WIDGET_IDS_KEY, ids);
        context.sendBroadcast(updateIntent);
    }
}
