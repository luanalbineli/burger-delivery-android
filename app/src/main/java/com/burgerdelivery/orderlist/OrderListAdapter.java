package com.burgerdelivery.orderlist;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.burgerdelivery.R;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.ui.RequestStatusView;
import com.burgerdelivery.ui.recyclerview.CustomRecyclerViewAdapter;

public class OrderListAdapter extends CustomRecyclerViewAdapter<OrderModel, OrderListVH> {

    OrderListAdapter(@StringRes int emptyMessageResId, @Nullable RequestStatusView.ITryAgainListener tryAgainClickListener) {
        super(emptyMessageResId, tryAgainClickListener);
    }

    @Override
    protected OrderListVH onCreateItemViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return new OrderListVH(layoutInflater.inflate(R.layout.order_item, parent, false));
    }

    @Override
    protected void onBindItemViewHolder(OrderListVH holder, int position) {
        OrderModel orderModel = getItemByPosition(position);
        holder.bind(orderModel);
    }
}
