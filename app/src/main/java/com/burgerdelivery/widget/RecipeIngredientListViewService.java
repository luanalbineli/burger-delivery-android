package com.burgerdelivery.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.R;
import com.burgerdelivery.dagger.component.DaggerInjectorComponent;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.repository.BurgerRepository;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;


public class RecipeIngredientListViewService extends RemoteViewsService {
    public static final String ORDER_ITEM_LIST_KEY = "order_item_list_key";
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        List<OrderItemModel> orderItemList = null;
        if (intent.getExtras() != null && intent.hasExtra(ORDER_ITEM_LIST_KEY)) {
            Timber.d("Contains the item list");
            Bundle bundle = intent.getBundleExtra(ORDER_ITEM_LIST_KEY);
            orderItemList = bundle.getParcelableArrayList(ORDER_ITEM_LIST_KEY);
        }

        return new WidgetRemoteViewsFactory(this, intent, orderItemList);
    }

    public static class WidgetRemoteViewsFactory implements RemoteViewsFactory {
        private final Context mContext;
        private final int mAppWidgetId;

        @Inject
        BurgerRepository mRecipeRepository;
        private @Nullable List<OrderItemModel> mOrderItemList;

        WidgetRemoteViewsFactory(Context context, Intent intent, @Nullable List<OrderItemModel> orderItemList) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            mOrderItemList = orderItemList;
            Timber.i("WidgetRemoteViewsFactory - WidgetId: " + mAppWidgetId);
        }

        @Override
        public void onCreate() {
            Timber.i("WidgetRemoteViewsFactory - onCreate()");
            DaggerInjectorComponent.builder()
                    .applicationComponent(BurgerDeliveryApplication.getApplicationComponent(mContext))
                    .build()
                    .inject(this);
            Timber.i("WidgetRemoteViewsFactory - onCreate() DONE!");
        }

        @Override
        public void onDataSetChanged() { }

        @Override
        public void onDestroy() { }

        @Override
        public int getCount() {
            return mOrderItemList == null ? 0 : mOrderItemList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_order_item);
            if (mOrderItemList != null) {
                OrderItemModel orderItemModel = mOrderItemList.get(position);

                remoteViews.setTextViewText(R.id.tvWidgetOrderItemQuantity, String.valueOf(orderItemModel.getQuantity()));

                remoteViews.setTextViewText(R.id.tvWidgetOrderItemBurgerName, orderItemModel.getBurgerModel().getName());
            }

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
