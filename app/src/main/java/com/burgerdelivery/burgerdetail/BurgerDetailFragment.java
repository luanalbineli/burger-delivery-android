package com.burgerdelivery.burgerdetail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.burgerdelivery.R;
import com.burgerdelivery.base.BaseFragment;
import com.burgerdelivery.base.BasePresenter;
import com.burgerdelivery.dagger.component.ApplicationComponent;
import com.burgerdelivery.dagger.component.DaggerInjectorComponent;
import com.burgerdelivery.model.BurgerModel;
import com.facebook.drawee.view.SimpleDraweeView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.burgerdelivery.util.Defaults.DEFAULT_PRICE_FORMAT;

public class BurgerDetailFragment extends BaseFragment<BurgerDetailContract.View> implements BurgerDetailContract.View {
    @Inject
    BurgerDetailPresenter mPresenter;

    @BindView(R.id.sdvBurgerDetailImage)
    SimpleDraweeView mBurgerImage;

    @BindView(R.id.tvBurgerDetailIngredients)
    TextView mBurgerIngredientsTextView;

    @BindView(R.id.tvBurgerDetailName)
    TextView mBurgerNameTextView;

    @BindView(R.id.tvBurgerDetailPrice)
    TextView mBurgerPriceTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.burger_detail, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.start((BurgerModel) getArguments().getParcelable(BURGER_MODEL_BK));
    }

    @Override
    public void showBurgerDetail(BurgerModel burgerModel) {
        mBurgerImage.setImageURI(burgerModel.getImageUrl());
        mBurgerNameTextView.setText(burgerModel.getName());
        mBurgerPriceTextView.setText(String.format(getString(R.string.price_format), DEFAULT_PRICE_FORMAT.format(burgerModel.getPrice())));

        mBurgerIngredientsTextView.setText(TextUtils.join(", ", burgerModel.getIngredients()));
    }

    @Override
    protected void onInjectDependencies(ApplicationComponent applicationComponent) {
        DaggerInjectorComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter<BurgerDetailContract.View> presenterImplementation() {
        return mPresenter;
    }

    @Override
    protected BurgerDetailContract.View viewImplementation() {
        return this;
    }

    public static Fragment getInstance(BurgerModel burgerModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BURGER_MODEL_BK, burgerModel);

        Fragment fragment = new BurgerDetailFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private static final String BURGER_MODEL_BK = "burger_model_bk";
}
