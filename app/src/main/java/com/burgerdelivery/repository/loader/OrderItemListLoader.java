package com.burgerdelivery.repository.loader;

import android.content.Context;

import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.repository.BurgerRepository;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class OrderItemListLoader extends AsyncTaskLoaderExecutor<OrderModel> {
    private final BurgerRepository mBurgerRepository;

    public OrderItemListLoader(Context context, BurgerRepository burgerRepository) {
        super(context);

        this.mBurgerRepository = burgerRepository;
    }

    @Override
    public OrderModel loadInBackground() {
        Timber.d("Fetching the order item list from the SQLite database.");
        try {
            OrderModel orderModel = mBurgerRepository.getCurrentPendingOrder().blockingGet();
            if (orderModel != OrderModel.EMPTY) {
                List<OrderItemModel> orderItemList = mBurgerRepository.getItemsByOrderId(orderModel.getId()).blockingGet();
                for (OrderItemModel orderItemModel : orderItemList) {
                    orderModel.addItemToOrder(orderItemModel);
                }
            }

            return orderModel;
        } catch (Exception e) {
            Timber.e(e, "An error occurred while tried to fetch the order item list");
            return null;
        }
    }
}
