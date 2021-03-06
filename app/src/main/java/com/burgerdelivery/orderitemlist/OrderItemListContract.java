package com.burgerdelivery.orderitemlist;

import android.support.annotation.Nullable;

import com.burgerdelivery.base.BasePresenter;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.model.viewmodel.BurgerListViewModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.model.viewmodel.OrderItemListViewModel;

import java.util.List;

interface OrderItemListContract {
    interface View {
        void closeScreen();

        void fetchOrderItemListUsingLoader();

        void showErrorLoadingOrder();

        void tryToFetchBurgerListUsingLoaderAgain();

        void showOrderItemList(List<OrderItemModel> data);

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showNoPendingOrderMessage();

        void disableFinishOrderButton();

        void updateOrderTotalValue(float total);

        void showConfirmationToRemoveOrderItem(int position);

        void showRemovingItemLoadingIndicator();

        void showMessageItemRemovedWithSuccess();

        void hideRemovingItemLoadingIndicator();

        void removeOrderItemFromList(int position);

        void showErrorRemovingOrderItem(Throwable throwable);

        void updateOrderItemByPosition(int position);

        void showFinishingOrderLoadingIndicator();

        void hideFinishingOrderLoadingIndicator();

        void showErrorFinishingOrder(Throwable throwable);

        void updateWidgets();
    }

    interface Presenter extends BasePresenter<View> {
        void onPendingOrderFetched(@Nullable OrderModel orderModel);

        void start(OrderItemListViewModel orderItemListViewModel);

        void tryToFetchBurgerListAgain();

        void onRemoveItem(int position);

        void removeItemConfirmed(int position);

        void onChangeOrderItemQuantity(int position, int value);

        void finishOrder();
    }
}
