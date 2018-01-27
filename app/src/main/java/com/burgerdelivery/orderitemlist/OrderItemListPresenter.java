package com.burgerdelivery.orderitemlist;

import android.support.annotation.Nullable;

import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.model.viewmodel.OrderItemListViewModel;
import com.burgerdelivery.repository.BurgerRepository;

import javax.inject.Inject;

import timber.log.Timber;

public class OrderItemListPresenter implements OrderItemListContract.Presenter {
    private OrderItemListContract.View mView;
    private OrderModel mOrderModel;

    private final BurgerRepository mBurgerRepository;

    @Inject
    OrderItemListPresenter(BurgerRepository burgerRepository) {
        mBurgerRepository = burgerRepository;
    }

    @Override
    public void setView(OrderItemListContract.View view) {
        mView = view;
    }

    @Override
    public void onPendingOrderFetched(@Nullable OrderModel orderModel) {
        mOrderModel = orderModel;
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

    @Override
    public void onRemoveItem(int position) {
        mView.showConfirmationToRemoveOrderItem(position);
    }

    @Override
    public void removeItemConfirmed(int position) {
        mView.showRemovingItemLoadingIndicator();
        OrderItemModel orderItemModel = mOrderModel.getItemList().get(position);
        mBurgerRepository.removeOrderItem(orderItemModel.getId())
                .doOnTerminate(mView::hideRemovingItemLoadingIndicator)
                .subscribe(
                        () -> {
                            mView.showMessageItemRemovedWithSuccess();
                            mView.removeOrderItemFromList(position);

                            mOrderModel.removeItemFromOrder(position);
                            mView.updateOrderTotalValue(mOrderModel.getTotalValue());
                        },
                            mView::showErrorRemovingOrderItem);
    }

    @Override
    public void onChangeOrderItemQuantity(int position, int value) {
        OrderItemModel orderItemModel = mOrderModel.getItemList().get(position);
        int newQuantity = orderItemModel.getQuantity() + value;
        if (newQuantity == 0) {
            return;
        }

        orderItemModel.setQuantity(newQuantity);

        mView.updateOrderItemByPosition(position);
        mView.updateOrderTotalValue(mOrderModel.getTotalValue());
    }

    @Override
    public void finishOrder() {
        mBurgerRepository.finishOrder(mOrderModel)
                .doOnSubscribe(disposable -> mView.showFinishingOrderLoadingIndicator())
                .doAfterTerminate(mView::hideFinishingOrderLoadingIndicator)
                .subscribe(finishOrderResponseModel -> {
                    mBurgerRepository.updateServerOrderId(mOrderModel.getId(), finishOrderResponseModel.getOrderId());
                }, mView::showErrorFinishingOrder);

    }
}
