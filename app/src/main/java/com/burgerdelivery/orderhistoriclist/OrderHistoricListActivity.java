package com.burgerdelivery.orderhistoriclist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.burgerdelivery.R;
import com.burgerdelivery.base.BaseActivity;
import com.burgerdelivery.base.BasePresenter;
import com.burgerdelivery.dagger.component.ApplicationComponent;
import com.burgerdelivery.dagger.component.DaggerInjectorComponent;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.model.viewmodel.HistoricOrderListViewModel;
import com.burgerdelivery.repository.BurgerRepository;
import com.burgerdelivery.repository.loader.HistoricOrderListLoader;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class OrderHistoricListActivity extends BaseActivity<OrderHistoricListContract.View> implements OrderHistoricListContract.View {
    @Inject
    OrderHistoricListPresenter mPresenter;

    @Inject
    BurgerRepository mBurgerRepository;

    @BindView(R.id.rvList)
    RecyclerView mHistoricOrderList;

    private OrderHistoricListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        ButterKnife.bind(this);

        configureRecyclerView();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.historic_order);
        }

        mPresenter.start(new HistoricOrderListViewModel());

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void configureRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mHistoricOrderList.setLayoutManager(linearLayoutManager);

        mAdapter = new OrderHistoricListAdapter(R.string.the_historic_order_list_is_empty, () -> mPresenter.tryToFetchHistoricOrderListAgain());

        mHistoricOrderList.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                closeScreen();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void closeScreen() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void fetchHistoricOrderItemListUsingLoader() {
        getLoaderManager().initLoader(TASK_ID, null, getLoaderCallback());
    }

    @Override
    public void tryToFetchHistoricOrderListUsingLoaderAgain() {
        getLoaderManager().restartLoader(TASK_ID, null, getLoaderCallback());
    }

    private LoaderManager.LoaderCallbacks<List<OrderModel>> getLoaderCallback() {
        return new LoaderManager.LoaderCallbacks<List<OrderModel>>() {

            @Override
            public Loader<List<OrderModel>> onCreateLoader(int id, Bundle args) {
                Timber.d("onCreateLoader - Creating the loader, id: " + id);
                return new HistoricOrderListLoader(OrderHistoricListActivity.this, mBurgerRepository);
            }

            @Override
            public void onLoadFinished(Loader<List<OrderModel>> loader, List<OrderModel> data) {
                Timber.i("onLoadFinished - Finished the loading: " + data);
                mPresenter.onHistoricOrderListFetched(data);
            }

            @Override
            public void onLoaderReset(Loader<List<OrderModel>> loader) {
                Timber.d("onLoaderReset - Reset the loader");
            }
        };
    }

    @Override
    public void showOrderItemList(List<OrderModel> historicOrderList) {
        mAdapter.addItems(historicOrderList);
    }

    @Override
    public void showErrorLoadingHistoricOrderList() {
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
    public void showHistoricOrderListEmptyMessage() {
        mAdapter.showEmptyMessage();
    }

    @Override
    protected void onInjectDependencies(ApplicationComponent applicationComponent) {
        DaggerInjectorComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter<OrderHistoricListContract.View> presenterImplementation() {
        return mPresenter;
    }

    @Override
    protected OrderHistoricListContract.View viewImplementation() {
        return this;
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, OrderHistoricListActivity.class);
    }

    private static final int TASK_ID = 108;
}
