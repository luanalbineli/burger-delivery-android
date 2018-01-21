package com.burgerdelivery.orderitemlist;

import android.support.annotation.Nullable;

import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.viewmodel.OrderItemListViewModel;

import java.util.List;

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
    public void onOrderItemListLoadingFinished(@Nullable List<OrderItemModel> data) {
        Timber.d("LOADED THE BURGER LIST: " + data);
        if (data == null) {
            mView.showErrorLoadingBurgerList();
        } else {
            mView.showOrderItemList(data);
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

    @Override
    public void handleBurgerItemClick(OrderItemModel orderItemModel) {
        mView.showBurgerDetail(orderItemModel);
    }
}
