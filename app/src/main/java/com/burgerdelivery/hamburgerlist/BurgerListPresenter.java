package com.burgerdelivery.hamburgerlist;

import android.support.annotation.Nullable;

import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.viewmodel.BurgerListViewModel;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class BurgerListPresenter implements BurgerListContract.Presenter {
    private BurgerListContract.View mView;

    @Inject
    public BurgerListPresenter() { }

    @Override
    public void setView(BurgerListContract.View view) {
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
}
