package com.burgerdelivery.burgerlist;

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
import com.burgerdelivery.burgerdetail.BurgerDetailFragment;
import com.burgerdelivery.dagger.component.ApplicationComponent;
import com.burgerdelivery.dagger.component.DaggerInjectorComponent;
import com.burgerdelivery.repository.HamburgerListLoader;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.viewmodel.BurgerListViewModel;
import com.burgerdelivery.ui.RequestStatusView;
import com.burgerdelivery.ui.recyclerview.CustomRecyclerViewAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import timber.log.Timber;

public class BurgerListFragment extends BaseFragment<BurgerListContract.View> implements BurgerListContract.View {
    @Inject
    BurgerListPresenter mPresenter;

    @Inject
    Retrofit mRetrofit;

    @BindView(R.id.rvList)
    RecyclerView mListRecyclerView;

    private BurgerListAdapter mAdapter;

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

        mAdapter = new BurgerListAdapter(R.string.burger_list_is_empty, new RequestStatusView.ITryAgainListener() {
            @Override
            public void tryAgain() {
                mPresenter.tryToFetchBurgerListAgain();
            }
        });

        mAdapter.setOnItemClickListener(new CustomRecyclerViewAdapter.IItemClickListener<BurgerModel>() {
            @Override
            public void click(int position, BurgerModel burgerModel) {
                mPresenter.handleBurgerItemClick(burgerModel);
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
    public void showBurgerDetail(BurgerModel burgerModel) {
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.flMainContent, BurgerDetailFragment.getInstance(burgerModel))
                .commit();
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
        return new BurgerListFragment();
    }

    private static final int TASK_ID = 103;
}
