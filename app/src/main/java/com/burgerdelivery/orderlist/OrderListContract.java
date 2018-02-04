package com.burgerdelivery.orderlist;

import com.burgerdelivery.base.BasePresenter;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.model.viewmodel.HistoricOrderListViewModel;

import java.util.List;

interface OrderListContract {
    interface View {
        void closeScreen();

        void fetchHistoricOrderItemListUsingLoader();

        void showErrorLoadingHistoricOrderList();

        void tryToFetchHistoricOrderListUsingLoaderAgain();

        void showOrderItemList(List<OrderModel> orderList);

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showHistoricOrderListEmptyMessage();
    }

    interface Presenter extends BasePresenter<View> {
        void onHistoricOrderListFetched(List<OrderModel> orderList);

        void start(HistoricOrderListViewModel historicOrderListViewModel);

        void tryToFetchHistoricOrderListAgain();
    }
}
