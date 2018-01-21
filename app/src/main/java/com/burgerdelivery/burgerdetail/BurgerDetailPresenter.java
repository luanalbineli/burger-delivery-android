package com.burgerdelivery.burgerdetail;

import com.burgerdelivery.enunn.OrderStatus;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.repository.BurgerRepository;
import com.burgerdelivery.util.BitFlag;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class BurgerDetailPresenter implements BurgerDetailContract.Presenter {
    private final BurgerRepository mBurgerRepository;
    private BurgerDetailContract.View mView;
    private BurgerModel mBurgerModel;

    @Inject
    BurgerDetailPresenter(BurgerRepository burgerRepository) {
        this.mBurgerRepository = burgerRepository;
    }

    @Override
    public void setView(BurgerDetailContract.View view) {
        mView = view;
    }

    @Override
    public void start(BurgerModel burgerModel) {
        mBurgerModel = burgerModel;
        mView.showBurgerDetail(burgerModel);
    }

    @Override
    public void addBurgerToOrder(BitFlag additional, String observation) {
        Timber.d("addBurgerToOrder - Started the creation of the item");
        mBurgerRepository.getCurrentPendingOrder().flatMap(orderModel -> {
            if (orderModel == OrderModel.EMPTY) {
                Timber.d("addBurgerToOrder - Need to create the pending order");
                return mBurgerRepository.insertOrder(new OrderModel(OrderStatus.PENDING, new Date()));
            }
            Timber.d("addBurgerToOrder - Already have a pending order: " + orderModel.getId());
            return Single.just(orderModel.getId());
        }).flatMap(orderId -> {
            Timber.d("addBurgerToOrder - Order id: " + orderId);
            OrderItemModel orderItemModel = new OrderItemModel(orderId, additional.getFlags(), observation, 0, mBurgerModel);

            return mBurgerRepository.insertBurgerItemIntoOrder(orderItemModel);
        }).doOnSubscribe(disposable -> mView.showLoadingIndicator())
                .doAfterTerminate(() -> mView.hideLoadingIndicator())
                .subscribe(itemOrderId -> {
                    Timber.d("Finished the creation of the order item");
                    mView.showMessageSuccessfullyCreatedOrderItem();
                    mView.returnToBurgerList();
                }, throwable -> {
                    Timber.e(throwable, "An error occurred while tried to save the order item");
                    mView.showErrorSavingOrderItem(throwable);

                });
    }
}
