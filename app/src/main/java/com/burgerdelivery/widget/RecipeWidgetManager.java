package com.burgerdelivery.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.burgerdelivery.R;
import com.burgerdelivery.model.OrderModel;

import java.util.ArrayList;

import timber.log.Timber;

abstract class RecipeWidgetManager {
    static void bindLayout(AppWidgetManager appWidgetManager, Context context, int widgetId, OrderModel orderModel) {
        Timber.d("Binding the layout for the widget id: " + widgetId);
        Timber.d("OrderModel: " + orderModel);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_final_layout);
        if (orderModel == OrderModel.EMPTY) {
            views.setViewVisibility(R.id.tvWidgetEmptyOrder, View.VISIBLE);
            views.setViewVisibility(R.id.llWidgetOrderDetailContainer, View.GONE);
        } else {
            views.setViewVisibility(R.id.tvWidgetEmptyOrder, View.GONE);
            views.setViewVisibility(R.id.llWidgetOrderDetailContainer, View.VISIBLE);

            views.setTextViewText(R.id.tvWidgetOrderStatus, orderModel.getStatusDescription(context));

            Intent intentAdapter = new Intent(context, RecipeIngredientListViewService.class);

            Bundle bundle = new Bundle(1);
            bundle.putParcelableArrayList(RecipeIngredientListViewService.ORDER_ITEM_LIST_KEY, new ArrayList<>(orderModel.getItemList()));
            intentAdapter.putExtra(RecipeIngredientListViewService.ORDER_ITEM_LIST_KEY, bundle);
            intentAdapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            views.setRemoteAdapter(R.id.lvWidgetItemList, intentAdapter);
            Timber.d("Bundled the item list");

            appWidgetManager.updateAppWidget(widgetId, views);
        }
    }
}
