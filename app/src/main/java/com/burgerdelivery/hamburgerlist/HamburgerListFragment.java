package com.burgerdelivery.hamburgerlist;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.burgerdelivery.R;
import com.burgerdelivery.base.BaseFragment;
import com.burgerdelivery.base.BasePresenter;
import com.burgerdelivery.dagger.component.ApplicationComponent;
import com.burgerdelivery.dagger.component.DaggerInjectorComponent;
import com.burgerdelivery.dao.HamburgerListLoader;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.viewmodel.BurgerListViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import timber.log.Timber;

public class HamburgerListFragment extends BaseFragment<BurgerListContract.View> implements BurgerListContract.View {
    @Inject
    BurgerListPresenter mPresenter;

    /*@Inject
    HamburgerListLoader mHamburgerListLoader;*/

    @Inject
    Retrofit mRetrofit;

    @BindView(R.id.rvList)
    RecyclerView mListRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        mPresenter.start(new BurgerListViewModel());
    }

    @Override
    public void fetchBurgerListUsingLoader() {
        getLoaderManager().initLoader(TASK_ID, new Bundle(), new LoaderManager.LoaderCallbacks<List<BurgerModel>>() {

            @Override
            public Loader<List<BurgerModel>> onCreateLoader(int id, Bundle args) {
                Timber.d("onCreateLoader - Creating the loader, id: " + id);
                return new HamburgerListLoader(getActivity(), mRetrofit);
            }

            @Override
            public void onLoadFinished(Loader<List<BurgerModel>> loader, List<BurgerModel> data) {
                Timber.i("onLoadFinished - Finished the loading: " + data);
                mPresenter.onBurgerListLoadingFinished(data);
            }

            @Override
            public void onLoaderReset(Loader<List<BurgerModel>> loader) {
                Timber.d("onLoaderReset - Reset the loader");
            }
        });
    }

    @Override
    public void showBurgerList(List<BurgerModel> data) {

    }

    @Override
    public void showErrorLoadingBurgerList() {

    }

    @Override
    public void showLoadingIndicator() {

    }

    @Override
    protected void onInjectDependencies(ApplicationComponent applicationComponent) {
        DaggerInjectorComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter<BurgerListContract.View> presenterImplementation() {
        return mPresenter;
    }

    @Override
    protected BurgerListContract.View viewImplementation() {
        return this;
    }

    public static Fragment getInstance() {
        return new HamburgerListFragment();
    }

    private static final int TASK_ID = 103;
}
