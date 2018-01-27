package com.burgerdelivery.orderitemlist;

import android.support.annotation.Nullable;

import com.burgerdelivery.base.BasePresenter;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.model.viewmodel.BurgerListViewModel;
import com.burgerdelivery.model.viewmodel.OrderItemListViewModel;

import java.util.List;

interface OrderItemListContract {
    interface View {
        void fetchOrderItemListUsingLoader();

        void showErrorLoadingOrder();

        void tryToFetchBurgerListUsingLoaderAgain();

        void showOrderItemList(List<OrderItemModel> data);

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showNoPendingOrderMessage();

        void disableFinishOrderButton();

        void updateOrderTotalValue(float total);
    }

    interface Presenter extends BasePresenter<View> {
        void onPendingOrderFetched(@Nullable OrderModel orderModel);

        void start(OrderItemListViewModel orderItemListViewModel);

        void tryToFetchBurgerListAgain();
    }
}
