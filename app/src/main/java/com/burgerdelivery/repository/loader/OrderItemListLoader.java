package com.burgerdelivery.repository.loader;

import android.content.Context;

import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.repository.BurgerRepository;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class OrderItemListLoader extends AsyncTaskLoaderExecutor<List<OrderItemModel>> {
    private final BurgerRepository mBurgerRepository;

    public OrderItemListLoader(Context context, BurgerRepository burgerRepository) {
        super(context);

        this.mBurgerRepository = burgerRepository;
    }

    @Override
    public List<OrderItemModel> loadInBackground() {
        Timber.d("Fetching the order item list from the SQLite database.");
        try {
            OrderModel orderModel = mBurgerRepository.getCurrentPendingOrder().blockingGet();
            if (orderModel == OrderModel.EMPTY) {
                return new ArrayList<>(0);
            }
            return mBurgerRepository.getItemsByOrderId(orderModel.getId()).blockingGet();
        } catch (Exception e) {
            Timber.e(e, "An error occurred while tried to fetch the order item list");
            return null;
        }
    }
}
