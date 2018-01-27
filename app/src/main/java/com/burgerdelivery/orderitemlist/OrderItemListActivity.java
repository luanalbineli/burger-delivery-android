package com.burgerdelivery.orderitemlist;

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
import android.widget.TextView;

import com.burgerdelivery.R;
import com.burgerdelivery.base.BaseActivity;
import com.burgerdelivery.base.BasePresenter;
import com.burgerdelivery.dagger.component.ApplicationComponent;
import com.burgerdelivery.dagger.component.DaggerInjectorComponent;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.model.viewmodel.OrderItemListViewModel;
import com.burgerdelivery.repository.BurgerRepository;
import com.burgerdelivery.repository.loader.OrderItemListLoader;
import com.ekalips.fancybuttonproj.FancyButton;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.burgerdelivery.util.Defaults.DEFAULT_PRICE_FORMAT;

public class OrderItemListActivity extends BaseActivity<OrderItemListContract.View> implements OrderItemListContract.View, OrderItemListAdapter.IChangeQuantityListener, OrderItemListAdapter.IRemoveOrderItemListener {
    @Inject
    OrderItemListPresenter mPresenter;

    @Inject
    BurgerRepository mBurgerRepository;

    @BindView(R.id.rvOrderItemList)
    RecyclerView mListRecyclerView;

    @BindView(R.id.fbOrderItemListFinishOrder)
    FancyButton mFinishOrderButton;

    @BindView(R.id.tvOrderItemListTotalValue)
    TextView mOrderTotalValueTextView;

    private OrderItemListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_list);

        ButterKnife.bind(this);

        configureRecyclerView();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.order_detail);
        }

        mPresenter.start(new OrderItemListViewModel());
    }

    private void configureRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mListRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new OrderItemListAdapter(R.string.item_order_list_is_empty, () -> mPresenter.tryToFetchBurgerListAgain(), this, this);

        mListRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void fetchOrderItemListUsingLoader() {
        getLoaderManager().initLoader(TASK_ID, null, getLoaderCallback());
    }

    @Override
    public void tryToFetchBurgerListUsingLoaderAgain() {
        getLoaderManager().restartLoader(TASK_ID, null, getLoaderCallback());
    }

    private LoaderManager.LoaderCallbacks<OrderModel> getLoaderCallback() {
        return new LoaderManager.LoaderCallbacks<OrderModel>() {

            @Override
            public Loader<OrderModel> onCreateLoader(int id, Bundle args) {
                Timber.d("onCreateLoader - Creating the loader, id: " + id);
                return new OrderItemListLoader(OrderItemListActivity.this, mBurgerRepository);
            }

            @Override
            public void onLoadFinished(Loader<OrderModel> loader, OrderModel data) {
                Timber.i("onLoadFinished - Finished the loading: " + data);
                mPresenter.onPendingOrderFetched(data);
            }

            @Override
            public void onLoaderReset(Loader<OrderModel> loader) {
                Timber.d("onLoaderReset - Reset the loader");
            }
        };
    }

    @Override
    public void showOrderItemList(List<OrderItemModel> orderItemList) {
        mAdapter.addItems(orderItemList);
    }

    @Override
    public void showErrorLoadingOrder() {
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
    public void showNoPendingOrderMessage() {
        mAdapter.showEmptyMessage();
    }

    @Override
    public void disableFinishOrderButton() {
        mFinishOrderButton.setEnabled(false);
    }

    @Override
    public void updateOrderTotalValue(float total) {
        mOrderTotalValueTextView.setText(String.format(getString(R.string.price_format), DEFAULT_PRICE_FORMAT.format(total)));
    }

    @Override
    public void onChangeQuantity(int position, int value) {

    }

    @Override
    public void removeOrderItem(int position) {

    }

    @Override
    protected void onInjectDependencies(ApplicationComponent applicationComponent) {
        DaggerInjectorComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter<OrderItemListContract.View> presenterImplementation() {
        return mPresenter;
    }

    @Override
    protected OrderItemListContract.View viewImplementation() {
        return this;
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, OrderItemListActivity.class);
    }

    private static final int TASK_ID = 108;
}
