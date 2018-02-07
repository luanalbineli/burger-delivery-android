package com.burgerdelivery.orderhistoriclist;

import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.model.viewmodel.HistoricOrderListViewModel;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class OrderHistoricListPresenter implements OrderHistoricListContract.Presenter {
    private OrderHistoricListContract.View mView;

    private HistoricOrderListViewModel mHistoricOrderListViewModel;

    @Inject
    OrderHistoricListPresenter() { }

    @Override
    public void setView(OrderHistoricListContract.View view) {
        mView = view;
    }

    @Override
    public void onHistoricOrderListFetched(List<OrderModel> orderList) {
        mHistoricOrderListViewModel.orderHistoricList = orderList;
        if (orderList.size() == 0) {
            mView.showHistoricOrderListEmptyMessage();
        } else {
            mView.showOrderItemList(orderList);
        }

        mView.hideLoadingIndicator();
    }

    @Override
    public void start(HistoricOrderListViewModel historicOrderListViewModel) {
        mHistoricOrderListViewModel = historicOrderListViewModel;
        Timber.d("Starting the fragment's presenter");
        if (historicOrderListViewModel.orderHistoricList == null) {
            mView.showLoadingIndicator();
            mView.fetchHistoricOrderItemListUsingLoader();
        } else {
            mView.showOrderItemList(historicOrderListViewModel.orderHistoricList);
            mView.updateWidgets();
        }
    }

    @Override
    public void tryToFetchHistoricOrderListAgain() {
        mView.showLoadingIndicator();
        mView.tryToFetchHistoricOrderListUsingLoaderAgain();
    }
}
