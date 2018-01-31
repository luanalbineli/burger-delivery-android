package com.burgerdelivery.orderitemlist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
    private MaterialDialog mCurrentDialog;

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

        mFinishOrderButton.setOnClickListener(view -> mPresenter.finishOrder());

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
    public void showConfirmationToRemoveOrderItem(int position) {
        Timber.d("Showing the confirmation dialog to remove the item on the position: " + position);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.confirmation)
                .content(R.string.order_item_confirmation_removal_message)
                .positiveText(R.string.confirm)
                .cancelable(true)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                    Timber.d("The user confirmed the exclusion");
                    mPresenter.removeItemConfirmed(position);
                });

        Drawable deleteIcon = AppCompatResources.getDrawable(this, R.drawable.delete);
        if (deleteIcon != null) {
            builder.icon(deleteIcon);
        }

        builder.show();
    }

    @Override
    public void showRemovingItemLoadingIndicator() {
        mCurrentDialog = new MaterialDialog.Builder(this)
                .title(R.string.removing_item)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
    }

    @Override
    public void showMessageItemRemovedWithSuccess() {
        Toast.makeText(this, R.string.item_removed_with_success_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideRemovingItemLoadingIndicator() {
        mCurrentDialog.dismiss();
    }

    @Override
    public void removeOrderItemFromList(int position) {
        mAdapter.removeItemByIndex(position);
    }

    @Override
    public void showErrorRemovingOrderItem(Throwable throwable) {
        Timber.e(throwable, "An error occurred while tried to remove the item from the order");
        Toast.makeText(this, R.string.item_was_not_removed_error_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateOrderItemByPosition(int position) {
        mAdapter.updateItemByIndex(position);
    }

    @Override
    public void showFinishingOrderLoadingIndicator() {
        mFinishOrderButton.collapse();
    }

    @Override
    public void hideFinishingOrderLoadingIndicator() {
        mFinishOrderButton.expand();
    }

    @Override
    public void showErrorFinishingOrder(Throwable throwable) {
        Timber.e(throwable, "An error occurred while tried to create the order");
        Toast.makeText(this, R.string.error_creating_order, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onChangeQuantity(int position, int value) {
        Timber.d("Changing the quantity: " + value);
        mPresenter.onChangeOrderItemQuantity(position, value);
    }

    @Override
    public void removeOrderItem(int position) {
        mPresenter.onRemoveItem(position);
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
