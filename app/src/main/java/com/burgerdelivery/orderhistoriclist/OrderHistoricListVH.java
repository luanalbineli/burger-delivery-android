package com.burgerdelivery.orderhistoriclist;

import android.view.View;
import android.widget.TextView;

import com.burgerdelivery.R;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.ui.recyclerview.CustomRecyclerViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.burgerdelivery.util.Defaults.DEFAULT_PRICE_FORMAT;

class OrderHistoricListVH extends CustomRecyclerViewHolder {

    @BindView(R.id.tvOrderHistoricId)
    TextView mOrderIdTextView;

    @BindView(R.id.tvHistoricOrderBurgerList)
    TextView mOrderBurgerListTextView;

    @BindView(R.id.tvOrderHistoricBurgerTotal)
    TextView mOrderBurgerTotalTextView;

    @BindView(R.id.tvOrderHistoricAdditionalTotal)
    TextView mOrderAdditionalTotalTextView;

    @BindView(R.id.tvOrderHistoricTotal)
    TextView mOrderTotalTextView;

    @BindView(R.id.tvOrderHistoricStatus)
    TextView mOrderStatusTextView;

    OrderHistoricListVH(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    void bind(OrderModel orderModel) {
        mOrderIdTextView.setText(String.valueOf(orderModel.getServerId()));

        String burgerList = buildBurgerList(orderModel.getItemList());
        mOrderBurgerListTextView.setText(burgerList);

        mOrderBurgerTotalTextView.setText(String.format(getContext().getString(R.string.price_format), DEFAULT_PRICE_FORMAT.format(orderModel.getBurgerTotal())));

        mOrderAdditionalTotalTextView.setText(String.format(getContext().getString(R.string.price_format), DEFAULT_PRICE_FORMAT.format(orderModel.getAdditionalTotal())));

        mOrderTotalTextView.setText(String.format(getContext().getString(R.string.price_format), DEFAULT_PRICE_FORMAT.format(orderModel.getTotalValue())));

        String statusFormat = String.format(getContext().getString(R.string.order_historic_status_format), orderModel.getStatusDescription(getContext()));
        mOrderStatusTextView.setText(statusFormat);
    }

    private String buildBurgerList(List<OrderItemModel> itemList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (OrderItemModel orderItemModel: itemList) {
            stringBuilder.append(orderItemModel.getQuantity())
                    .append(" - ")
                    .append(orderItemModel.getBurgerModel().getName());
        }

        return stringBuilder.toString();
    }
}
