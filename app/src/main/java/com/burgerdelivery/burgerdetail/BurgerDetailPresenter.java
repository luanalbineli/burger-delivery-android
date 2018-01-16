package com.burgerdelivery.burgerdetail;

import com.burgerdelivery.enunn.AdditionalItemStatus;
import com.burgerdelivery.enunn.OrderStatus;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.repository.BurgerRepository;

import java.util.Date;
import java.util.EnumSet;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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
    public void addBurgerToOrder(EnumSet<AdditionalItemStatus> additionalEnumSet, String observation) {
        mView.showLoadingIndicator();

        mBurgerRepository.getCurrentPendingOrder().map(orderModel -> {
            if (orderModel == null) {
                return mBurgerRepository.insertOrder(new OrderModel(OrderStatus.PENDING, new Date()));
            }
            return Single.just(orderModel.getId());
        }).map(integerSingle -> integerSingle.blockingGet()).map(new Function<Integer, Completable>() {
            @Override
            public Completable apply(Integer orderId) throws Exception {
                OrderItemModel orderItemModel = new OrderItemModel(orderId, additionalEnumSet, mBurgerModel);

                return mBurgerRepository.insertBurgerItemIntoOrder(orderItemModel);
            }
        }).subscribe(new Consumer<Completable>() {
            @Override
            public void accept(Completable completable) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }
}
