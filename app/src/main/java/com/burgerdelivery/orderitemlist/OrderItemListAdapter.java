package com.burgerdelivery.orderitemlist;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.burgerdelivery.R;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.ui.RequestStatusView;
import com.burgerdelivery.ui.recyclerview.CustomRecyclerViewAdapter;

public class OrderItemListAdapter extends CustomRecyclerViewAdapter<BurgerModel, OrderItemListVH> {
    OrderItemListAdapter(@StringRes int emptyMessageResId, @Nullable RequestStatusView.ITryAgainListener tryAgainClickListener) {
        super(emptyMessageResId, tryAgainClickListener);
    }

    @Override
    protected OrderItemListVH onCreateItemViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return new OrderItemListVH(layoutInflater.inflate(R.layout.burger_item, parent, false));
    }

    @Override
    protected void onBindItemViewHolder(OrderItemListVH holder, int position) {
        BurgerModel burgerModel = getItemByPosition(position);
        holder.bind(burgerModel);
    }
}
