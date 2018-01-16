package com.burgerdelivery.burgerdetail;

import com.burgerdelivery.enunn.AdditionalItemStatus;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.repository.BurgerRepository;

import java.util.EnumSet;

import javax.inject.Inject;

public class BurgerDetailPresenter implements BurgerDetailContract.Presenter {
    private final BurgerRepository mBurgerRepository;
    private BurgerDetailContract.View mView;
    private BurgerModel mBurgerModel;

    @Inject
    BurgerDetailPresenter(BurgerRepository burgerRepository) {
        this.mBurgerRepository = burgerRepository;
    }

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
        mView.showLoadingIndicator();

        mBurgerRepository.
        mBurgerRepository.insertBurgerItemIntoOrder()
    }
}
