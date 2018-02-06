package com.burgerdelivery.orderhistoriclist;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.burgerdelivery.R;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.ui.RequestStatusView;
import com.burgerdelivery.ui.recyclerview.CustomRecyclerViewAdapter;

public class OrderHistoricListAdapter extends CustomRecyclerViewAdapter<OrderModel, OrderHistoricListVH> {

    OrderHistoricListAdapter(@StringRes int emptyMessageResId, @Nullable RequestStatusView.ITryAgainListener tryAgainClickListener) {
        super(emptyMessageResId, tryAgainClickListener);
    }

    @Override
    protected OrderHistoricListVH onCreateItemViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return new OrderHistoricListVH(layoutInflater.inflate(R.layout.order_historic_item, parent, false));
    }

    @Override
    protected void onBindItemViewHolder(OrderHistoricListVH holder, int position) {
        OrderModel orderModel = getItemByPosition(position);
        holder.bind(orderModel);
    }
}
