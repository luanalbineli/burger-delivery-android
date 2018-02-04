package com.burgerdelivery.orderlist;

import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.model.viewmodel.HistoricOrderListViewModel;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class OrderListPresenter implements OrderListContract.Presenter {
    private OrderListContract.View mView;

    private HistoricOrderListViewModel mHistoricOrderListViewModel;

    @Inject
    OrderListPresenter() { }

    @Override
    public void setView(OrderListContract.View view) {
        mView = view;
    }

    @Override
    public void onHistoricOrderListFetched(List<OrderModel> orderList) {
        mHistoricOrderListViewModel.orderList = orderList;
        if (orderList.size() == 0) {
            mView.showErrorLoadingHistoricOrderList();
        } else {
            mView.showOrderItemList(orderList);
        }

        mView.hideLoadingIndicator();
    }

    @Override
    public void start(HistoricOrderListViewModel historicOrderListViewModel) {
        mHistoricOrderListViewModel = historicOrderListViewModel;
        Timber.d("Starting the fragment's presenter");
        if (historicOrderListViewModel.orderList == null) {
            mView.showLoadingIndicator();
            mView.fetchHistoricOrderItemListUsingLoader();
        } else {
            mView.showOrderItemList(historicOrderListViewModel.orderList);
        }
    }

    @Override
    public void tryToFetchHistoricOrderListAgain() {
        mView.showLoadingIndicator();
        mView.tryToFetchHistoricOrderListUsingLoaderAgain();
    }
}
