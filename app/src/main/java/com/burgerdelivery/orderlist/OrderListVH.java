package com.burgerdelivery.orderlist;

import android.view.View;

import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.ui.recyclerview.CustomRecyclerViewHolder;

import butterknife.ButterKnife;

class OrderListVH extends CustomRecyclerViewHolder {

    OrderListVH(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    void bind(OrderModel orderModel) {

    }
}