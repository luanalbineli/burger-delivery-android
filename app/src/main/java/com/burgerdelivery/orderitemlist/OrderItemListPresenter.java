package com.burgerdelivery.orderitemlist;

import android.support.annotation.Nullable;

import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.model.viewmodel.OrderItemListViewModel;

import javax.inject.Inject;

import timber.log.Timber;

public class OrderItemListPresenter implements OrderItemListContract.Presenter {
    private OrderItemListContract.View mView;

    @Inject
    OrderItemListPresenter() { }

    @Override
    public void setView(OrderItemListContract.View view) {
        mView = view;
    }

    @Override
    public void onPendingOrderFetched(@Nullable OrderModel orderModel) {
        if (orderModel == null) {
            mView.showErrorLoadingOrder();
        } else if (orderModel == OrderModel.EMPTY) {
            mView.showNoPendingOrderMessage();
            mView.disableFinishOrderButton();
        } else {
            mView.showOrderItemList(orderModel.getItemList());
            mView.updateOrderTotalValue(orderModel.getTotalValue());
        }

        mView.hideLoadingIndicator();
    }

    @Override
    public void start(OrderItemListViewModel orderItemListViewModel) {
        Timber.d("Starting the fragment's presenter");
        if (orderItemListViewModel.orderItemList == null) {
            mView.showLoadingIndicator();
            mView.fetchOrderItemListUsingLoader();
        } else {
            mView.showOrderItemList(orderItemListViewModel.orderItemList);
        }
    }

    @Override
    public void tryToFetchBurgerListAgain() {
        mView.showLoadingIndicator();
        mView.tryToFetchBurgerListUsingLoaderAgain();
    }
}
