package com.burgerdelivery.orderitemlist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.burgerdelivery.R;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.ui.recyclerview.CustomRecyclerViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

class OrderItemListVH extends CustomRecyclerViewHolder {
    @BindView(R.id.sdvOrderItemBurgerImage)
    SimpleDraweeView burgerImageSimpleDraweeView;

    @BindView(R.id.tvOrderItemBurgerName)
    TextView burgerNameTextView;

    @BindView(R.id.ivOrderItemRemove)
    ImageView orderItemRemoveImageView;

    @BindView(R.id.ivOrderItemQuantityAdd)
    ImageView orderItemQuantityAddImageView;

    @BindView(R.id.ivOrderItemQuantityRemove)
    ImageView orderItemQuantityRemoveImageView;

    @BindView(R.id.tvOrderItemQuantity)
    TextView orderItemQuantityTextView;

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
