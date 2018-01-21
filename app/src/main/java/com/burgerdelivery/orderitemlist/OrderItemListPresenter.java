package com.burgerdelivery.orderitemlist;

import android.support.annotation.Nullable;

import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.viewmodel.BurgerListViewModel;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class OrderItemListPresenter implements OrderItemListContract.Presenter {
    private OrderItemListContract.View mView;

    @Inject
    OrderItemListPresenter() { }

    @Override
    public void setView(OrderItemListContract.View view) {
        mView = view;
    }

    @Override
    public void onBurgerListLoadingFinished(@Nullable List<BurgerModel> data) {
        Timber.d("LOADED THE BURGER LIST: " + data);
        if (data == null) {
            mView.showErrorLoadingBurgerList();
        } else {
            mView.showBurgerList(data);
        }

        mView.hideLoadingIndicator();
    }

    @Override
    public void start(BurgerListViewModel burgerListViewModel) {
        Timber.d("Starting the fragment's presenter");
        if (burgerListViewModel.burgerList == null) {
            mView.showLoadingIndicator();
            mView.fetchBurgerListUsingLoader();
        } else {
            mView.showBurgerList(burgerListViewModel.burgerList);
        }
    }

    @Override
    public void tryToFetchBurgerListAgain() {
        mView.showLoadingIndicator();
        mView.tryToFetchBurgerListUsingLoaderAgain();
    }

    @Override
    public void handleBurgerItemClick(BurgerModel burgerModel) {
        mView.showBurgerDetail(burgerModel);
    }
}
