package com.burgerdelivery.orderhistoriclist;

import android.view.View;

import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.ui.recyclerview.CustomRecyclerViewHolder;

import butterknife.ButterKnife;

class OrderHistoricListVH extends CustomRecyclerViewHolder {

    OrderHistoricListVH(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    void bind(OrderModel orderModel) {

    }
}
