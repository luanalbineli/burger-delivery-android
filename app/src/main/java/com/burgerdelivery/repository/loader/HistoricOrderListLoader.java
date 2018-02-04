package com.burgerdelivery.repository.loader;

import android.content.Context;

import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.repository.BurgerRepository;

import java.util.List;

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
            return mBurgerRepository.getHistoricOrderList().blockingGet();
        } catch (Exception e) {
            Timber.e(e, "An error occurred while tried to fetch the historic order list");
            return null;
        }
    }
}
