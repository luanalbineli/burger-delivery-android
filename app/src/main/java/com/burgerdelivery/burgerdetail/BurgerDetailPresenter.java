package com.burgerdelivery.burgerdetail;

import com.burgerdelivery.model.BurgerModel;

import javax.inject.Inject;

public class BurgerDetailPresenter implements BurgerDetailContract.Presenter {
    private BurgerDetailContract.View mView;

    @Inject
    BurgerDetailPresenter() { }

    @Override
    public void setView(BurgerDetailContract.View view) {
        mView = view;
    }

    @Override
    public void start(BurgerModel burgerModel) {
        mView.showBurgerDetail(burgerModel);
    }
}
