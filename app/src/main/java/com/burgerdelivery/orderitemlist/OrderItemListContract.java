package com.burgerdelivery.orderitemlist;

import com.burgerdelivery.base.BasePresenter;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.viewmodel.BurgerListViewModel;
import com.burgerdelivery.model.viewmodel.OrderItemListViewModel;

import java.util.List;

interface OrderItemListContract {
    interface View {
        void fetchOrderItemListUsingLoader();

        void showErrorLoadingBurgerList();

        void tryToFetchBurgerListUsingLoaderAgain();

        void showOrderItemList(List<OrderItemModel> data);

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showBurgerDetail(OrderItemModel orderItemModel);

        void showEmptyOrderListMessage();
    }

    interface Presenter extends BasePresenter<View> {
        void onOrderItemListLoadingFinished(List<OrderItemModel> data);

        void start(OrderItemListViewModel orderItemListViewModel);

        void tryToFetchBurgerListAgain();

        void handleBurgerItemClick(OrderItemModel orderItemModel);
    }
}
