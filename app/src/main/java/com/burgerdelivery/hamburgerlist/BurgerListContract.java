package com.burgerdelivery.hamburgerlist;

import com.burgerdelivery.base.BasePresenter;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.viewmodel.BurgerListViewModel;

import java.util.List;

interface BurgerListContract {
    interface View {
        void fetchBurgerListUsingLoader();

        void showErrorLoadingBurgerList();

        void showBurgerList(List<BurgerModel> data);

        void showLoadingIndicator();
    }

    interface Presenter extends BasePresenter<View> {
        void onBurgerListLoadingFinished(List<BurgerModel> data);

        void start(BurgerListViewModel burgerListViewModel);
    }
}
