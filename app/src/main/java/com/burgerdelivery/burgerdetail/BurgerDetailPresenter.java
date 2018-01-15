package com.burgerdelivery.burgerdetail;

import com.burgerdelivery.enunn.AdditionalItemStatus;
import com.burgerdelivery.model.BurgerModel;

import java.util.EnumSet;

import javax.inject.Inject;

public class BurgerDetailPresenter implements BurgerDetailContract.Presenter {
    private BurgerDetailContract.View mView;
    private BurgerModel mBurgerModel;

    @Inject
    BurgerDetailPresenter() { }

    @Override
    public void setView(BurgerDetailContract.View view) {
        mView = view;
    }

    @Override
    public void start(BurgerModel burgerModel) {
        mBurgerModel = burgerModel;
        mView.showBurgerDetail(burgerModel);
    }

    @Override
    public void addBurgerToOrder(EnumSet<AdditionalItemStatus> additionalEnumSet, String observation) {

    }
}
