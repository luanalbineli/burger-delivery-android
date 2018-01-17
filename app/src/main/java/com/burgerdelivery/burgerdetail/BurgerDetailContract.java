package com.burgerdelivery.burgerdetail;

import com.burgerdelivery.base.BasePresenter;
import com.burgerdelivery.enunn.AdditionalItemStatus;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.viewmodel.BurgerListViewModel;
import com.burgerdelivery.util.BitFlag;

import java.util.EnumSet;
import java.util.List;

interface BurgerDetailContract {
    interface View {
        void showBurgerDetail(BurgerModel burgerModel);

        void showLoadingIndicator();

        void showErrorSavingOrderItem(Throwable throwable);

        void showMessageSuccessfullyCreatedOrderItem();

        void returnToBurgerList();

        void hideLoadingIndicator();
    }

    interface Presenter extends BasePresenter<View> {
        void start(BurgerModel burgerModel);

        void addBurgerToOrder(BitFlag additional, String observation);
    }
}
