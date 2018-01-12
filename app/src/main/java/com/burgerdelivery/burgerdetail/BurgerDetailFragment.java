package com.burgerdelivery.burgerdetail;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import com.burgerdelivery.ui.RequestStatusView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import timber.log.Timber;

public class BurgerDetailFragment extends BaseFragment<BurgerDetailContract.View> implements BurgerDetailContract.View {
    @Inject
    BurgerDetailPresenter mPresenter;

    @Inject
    Retrofit mRetrofit;

    @BindView(R.id.rvList)
    RecyclerView mListRecyclerView;

    private BurgerIngredientAdapter mAdapter;

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

        configureRecyclerView();

        mPresenter.start(new BurgerListViewModel());
    }

    private void configureRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mListRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        mListRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new BurgerIngredientAdapter(R.string.burger_list_is_empty, new RequestStatusView.ITryAgainListener() {
            @Override
            public void tryAgain() {
                mPresenter.tryToFetchBurgerListAgain();
            }
        });

        mListRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fetchBurgerListUsingLoader() {
        getLoaderManager().initLoader(TASK_ID, null, getLoaderCallback());
    }

    @Override
    public void tryToFetchBurgerListUsingLoaderAgain() {
        getLoaderManager().restartLoader(TASK_ID, null, getLoaderCallback());
    }

    private LoaderManager.LoaderCallbacks<List<BurgerModel>> getLoaderCallback() {
        return new LoaderManager.LoaderCallbacks<List<BurgerModel>>() {

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
        };
    }

    @Override
    public void showBurgerList(List<BurgerModel> burgerList) {
        mAdapter.addItems(burgerList);
    }

    @Override
    public void showErrorLoadingBurgerList() {
        mAdapter.showErrorMessage();
    }

    @Override
    public void showLoadingIndicator() {
        mAdapter.showLoading();
    }

    @Override
    public void hideLoadingIndicator() {
        mAdapter.hideLoadingIndicator();
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

    public static Fragment getInstance() {
        return new BurgerDetailFragment();
    }

    private static final int TASK_ID = 103;
}
