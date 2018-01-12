package com.burgerdelivery.burgerdetail;

import com.burgerdelivery.base.BasePresenter;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.viewmodel.BurgerListViewModel;

import java.util.List;

interface BurgerDetailContract {
    interface View {
        void fetchBurgerListUsingLoader();

        void showErrorLoadingBurgerList();

        void tryToFetchBurgerListUsingLoaderAgain();

        void showBurgerList(List<BurgerModel> data);

        void showLoadingIndicator();

        void hideLoadingIndicator();
    }

    interface Presenter extends BasePresenter<View> {
        void onBurgerListLoadingFinished(List<BurgerModel> data);

        void start(BurgerListViewModel burgerListViewModel);

        void tryToFetchBurgerListAgain();
    }
}
