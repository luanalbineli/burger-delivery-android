package com.burgerdelivery.orderitemlist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.burgerdelivery.R;
import com.burgerdelivery.enunn.AdditionalItemStatus;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.ui.recyclerview.CustomRecyclerViewHolder;
import com.burgerdelivery.util.BitFlag;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.burgerdelivery.util.Defaults.DEFAULT_PRICE_FORMAT;

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

    @BindView(R.id.tvOrderItemAdditional)
    TextView orderItemAdditionalTextView;

    @BindView(R.id.tvOrderItemSubtotalValue)
    TextView orderItemSubtotalValueTextView;


    @BindView(R.id.tvOrderItemAdditionalValue)
    TextView orderItemAdditionalValue;

    @BindView(R.id.tvOrderItemTotalValue)
    TextView orderItemTotalValue;

    OrderItemListVH(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    void bind(OrderItemModel orderItemModel, OrderItemListAdapter.IChangeQuantityListener changeQuantityListener, OrderItemListAdapter.IRemoveOrderItemListener removeOrderItemListener) {
        burgerImageSimpleDraweeView.setImageURI(orderItemModel.getBurgerModel().getImageUrl());

        burgerNameTextView.setText(orderItemModel.getBurgerModel().getName());

        orderItemRemoveImageView.setOnClickListener(v -> removeOrderItemListener.removeOrderItem(getAdapterPosition()));

        orderItemQuantityRemoveImageView.setOnClickListener(v -> changeQuantityListener.onChangeQuantity(getAdapterPosition(), -1));

        orderItemQuantityAddImageView.setOnClickListener(v -> changeQuantityListener.onChangeQuantity(getAdapterPosition(), 1));

        orderItemQuantityTextView.setText(String.valueOf(orderItemModel.getQuantity()));

        orderItemAdditionalTextView.setText(buildAdditionalLabel(orderItemModel.getAdditional()));

        orderItemSubtotalValueTextView.setText(String.format(getContext().getString(R.string.price_format), DEFAULT_PRICE_FORMAT.format(orderItemModel.getSubtotalValue())));

        orderItemAdditionalValue.setText(String.format(getContext().getString(R.string.price_format), DEFAULT_PRICE_FORMAT.format(orderItemModel.getAdditionalValue())));

        orderItemTotalValue.setText(String.format(getContext().getString(R.string.price_format), DEFAULT_PRICE_FORMAT.format(orderItemModel.getTotal())));
    }

    private String buildAdditionalLabel(int additional) {
        if (additional == 0) {
            return getContext().getString(R.string.order_item_list_total_default);
        }
        BitFlag bitFlag = new BitFlag(additional);
        StringBuilder additionalString = new StringBuilder();
        if (bitFlag.isSet(AdditionalItemStatus.BURGER)) {
            additionalString.append(getContext().getString(R.string.additional_burger));
        }

        if (bitFlag.isSet(AdditionalItemStatus.CHEDDAR)) {
            if (additionalString.length() > 0) {
                additionalString.append(", ");
            }
            additionalString.append(getContext().getString(R.string.additional_cheddar));
        }

        if (bitFlag.isSet(AdditionalItemStatus.PICKLES)) {
            if (additionalString.length() > 0) {
                additionalString.append(", ");
            }
            additionalString.append(getContext().getString(R.string.additional_pickles));
        }

        return additionalString.toString();
    }
}
