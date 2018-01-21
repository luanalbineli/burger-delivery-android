package com.burgerdelivery.orderitemlist;

import android.view.View;
import android.widget.TextView;

import com.burgerdelivery.R;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.ui.recyclerview.CustomRecyclerViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.burgerdelivery.util.Defaults.DEFAULT_PRICE_FORMAT;

class OrderItemListVH extends CustomRecyclerViewHolder {
    @BindView(R.id.sdvBurgerItemImage)
    SimpleDraweeView burgerImageSimpleDraweeView;

    @BindView(R.id.tvBurgerItemName)
    TextView burgerNameTextView;

    @BindView(R.id.tvBurgerItemPrice)
    TextView burgerPriceTextView;

    @BindView(R.id.tvBurgerItemDescription)
    TextView burgerDescriptionTextView;

    OrderItemListVH(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    void bind(OrderItemModel orderItemModel) {
        /*burgerNameTextView.setText(orderItemModel.getName());
        burgerDescriptionTextView.setText(orderItemModel.getDescription());
        burgerPriceTextView.setText(String.format(getContext().getString(R.string.price_format), DEFAULT_PRICE_FORMAT.format(orderItemModel.getPrice())));

        burgerImageSimpleDraweeView.setImageURI(orderItemModel.getImageUrl());*/
    }
}
