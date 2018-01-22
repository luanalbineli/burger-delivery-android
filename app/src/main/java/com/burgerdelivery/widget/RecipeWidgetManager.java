package com.burgerdelivery.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.burgerdelivery.R;
import com.burgerdelivery.model.OrderModel;

import timber.log.Timber;

abstract class RecipeWidgetManager {
    static void bindLayout(AppWidgetManager appWidgetManager, Context context, int widgetId, OrderModel recipeModel) {
        Timber.d("bindLayout - Binding the layout for the widget: " + widgetId);
        Timber.d("bindLayout - recipeModel: " + recipeModel);
       /* RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_final_layout);

        views.setTextViewText(R.id.tvWidgetRecipeName, recipeModel.getName());

        Intent intentAdapter = new Intent(context, RecipeIngredientListViewService.class);
        intentAdapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        views.setRemoteAdapter(R.id.lvWidgetIngredientList, intentAdapter);

        appWidgetManager.updateAppWidget(widgetId, views);*/
    }
}
