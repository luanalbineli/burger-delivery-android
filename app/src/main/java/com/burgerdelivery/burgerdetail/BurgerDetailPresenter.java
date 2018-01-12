package com.burgerdelivery.burgerdetail;

import android.support.annotation.Nullable;

import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.viewmodel.BurgerListViewModel;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class BurgerDetailPresenter implements BurgerDetailContract.Presenter {
    private BurgerDetailContract.View mView;

    @Inject
    BurgerDetailPresenter() { }

    @Override
    public void setView(BurgerDetailContract.View view) {
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
}
