package com.burgerdelivery.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.R;
import com.burgerdelivery.dagger.component.DaggerInjectorComponent;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.repository.BurgerRepository;

import javax.inject.Inject;

import timber.log.Timber;


public class RecipeIngredientListViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this, intent);
    }

    public static class WidgetRemoteViewsFactory implements RemoteViewsFactory {
        private final Context mContext;
        private final int mAppWidgetId;

        @Inject
        BurgerRepository mRecipeRepository;
        private OrderModel mOrderModel;

        WidgetRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
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
        public void onDataSetChanged() {
            mOrderModel = mRecipeRepository.getCurrentPendingOrder().blockingGet();
            Timber.i("WidgetRemoteViewsFactory - recipe model: " + mOrderModel);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mOrderModel.getItemList().size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            OrderItemModel orderItemModel = mOrderModel.getItemList().get(position);
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_order_item);

            remoteViews.setTextViewText(R.id.tvWidgetOrderItemQuantity, String.valueOf(orderItemModel.getQuantity()));

            remoteViews.setTextViewText(R.id.tvWidgetOrderItemBurgerName, orderItemModel.getBurgerModel().getName());
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
