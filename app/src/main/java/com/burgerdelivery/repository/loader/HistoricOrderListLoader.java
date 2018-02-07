package com.burgerdelivery.repository.loader;

import android.content.Context;
import android.util.Pair;

import com.burgerdelivery.enunn.OrderStatus;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.model.response.OrderStatusUpdateModel;
import com.burgerdelivery.repository.BurgerRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import timber.log.Timber;

public class HistoricOrderListLoader extends AsyncTaskLoaderExecutor<List<OrderModel>> {
    private final BurgerRepository mBurgerRepository;

    public HistoricOrderListLoader(Context context, BurgerRepository burgerRepository) {
        super(context);

        this.mBurgerRepository = burgerRepository;
    }

    @Override
    public List<OrderModel> loadInBackground() {
        Timber.d("Fetching the order list from the SQLite database.");
        try {
            return mBurgerRepository.getHistoricOrderList()
                    .map(orderList -> {
                        List<Integer> nonPendingUndeliveredOrderList = new ArrayList<>();
                        for (OrderModel orderModel : orderList) {
                            if (orderModel.getStatus() != OrderStatus.PENDING && orderModel.getStatus() != OrderStatus.DELIVERED) {
                                nonPendingUndeliveredOrderList.add(orderModel.getServerId());
                            }
                        }
                        return new Pair<>(orderList, nonPendingUndeliveredOrderList);
                    }).flatMap(listListPair -> {
                        Timber.d("Checking if there is some orders to update the status");
                        if (listListPair.second.size() == 0) {
                            return Single.just(listListPair.first);
                        }
                        Timber.d("Yes, there is");
                        return mBurgerRepository.updateOrderListStatus(listListPair.second)
                                .flatMap(orderStatusUpdateList -> {
                                    List<Single<Integer>> singleList = new ArrayList<>();
                                    for (OrderStatusUpdateModel orderStatusUpdateModel: orderStatusUpdateList) {
                                        singleList.add(mBurgerRepository.updateOrderStatusByServerId(orderStatusUpdateModel.getId(), orderStatusUpdateModel.getStatus()).toSingleDefault(1));
                                    }

                                    return Single.zip(singleList, objects -> {
                                        for (OrderStatusUpdateModel orderStatusUpdateModel: orderStatusUpdateList) {
                                            for (OrderModel orderModel : listListPair.first) {
                                                if (orderModel.getServerId() == orderStatusUpdateModel.getId()) {
                                                    orderModel.setStatus(orderStatusUpdateModel.getStatus());
                                                }
                                            }
                                        }
                                        return listListPair.first;
                                    });
                                });
                    }).blockingGet();
        } catch (Exception e) {
            Timber.e(e, "An error occurred while tried to fetch the historic order list");
            return null;
        }
    }
}
