package com.burgerdelivery.orderitemlist;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.burgerdelivery.R;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.ui.RequestStatusView;
import com.burgerdelivery.ui.recyclerview.CustomRecyclerViewAdapter;

public class OrderItemListAdapter extends CustomRecyclerViewAdapter<OrderItemModel, OrderItemListVH> {
    private final IChangeQuantityListener mChangeQuantityListener;
    private final IRemoveOrderItemListener mRemoveOrderItemListener;

    OrderItemListAdapter(@StringRes int emptyMessageResId, @Nullable RequestStatusView.ITryAgainListener tryAgainClickListener, IChangeQuantityListener changeQuantityListener, IRemoveOrderItemListener removeOrderItemListener) {
        super(emptyMessageResId, tryAgainClickListener);

        this.mChangeQuantityListener = changeQuantityListener;

        this.mRemoveOrderItemListener = removeOrderItemListener;
    }

    @Override
    protected OrderItemListVH onCreateItemViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return new OrderItemListVH(layoutInflater.inflate(R.layout.order_item, parent, false));
    }

    @Override
    protected void onBindItemViewHolder(OrderItemListVH holder, int position) {
        OrderItemModel orderItemModel = getItemByPosition(position);
        holder.bind(orderItemModel, mChangeQuantityListener, mRemoveOrderItemListener);
    }

    interface IChangeQuantityListener {
        void onChangeQuantity(int position, int value);
    }

    interface IRemoveOrderItemListener {
        void removeOrderItem(int position);
    }
}
