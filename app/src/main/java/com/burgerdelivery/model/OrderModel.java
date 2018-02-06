package com.burgerdelivery.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.burgerdelivery.R;
import com.burgerdelivery.enunn.OrderStatus;
import com.burgerdelivery.repository.contentprovider.BurgerDeliveryContract;
import com.burgerdelivery.util.SQLUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class OrderModel {
    public static final OrderModel EMPTY = new OrderModel(Integer.MIN_VALUE);

    private List<OrderItemModel> itemList;
    private Date date;
    private int status;
    private int id;
    private int serverId;

    public OrderModel(@OrderStatus int status, Date date) {
        this.status = status;
        this.date = date;
    }

    private OrderModel(int id, @OrderStatus int status, Date date, int serverId) {
        this(status, date);

        this.id = id;
        this.serverId = serverId;
    }

    private OrderModel(int id) {
        this.id = id;
    }

    public void addItemToOrder(OrderItemModel orderItemModel) {
        preCheckItemList();
        itemList.add(orderItemModel);
    }

    public List<OrderItemModel> getItemList() {
        preCheckItemList();
        return itemList;
    }

    private void preCheckItemList() {
        if (itemList == null) {
            itemList = new ArrayList<>();
        }
    }

    public static OrderModel fromCursor(Cursor cursor) {
        return new OrderModel(
                SQLUtil.getInt(cursor, BurgerDeliveryContract.OrderEntry._ID),
                SQLUtil.getInt(cursor, BurgerDeliveryContract.OrderEntry.COLUMN_STATUS),
                SQLUtil.getDate(cursor, BurgerDeliveryContract.OrderEntry.COLUMN_DATE),
                SQLUtil.getInt(cursor, BurgerDeliveryContract.OrderEntry.COLUMN_SERVER_ID)
        );
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BurgerDeliveryContract.OrderEntry.COLUMN_STATUS, status);
        contentValues.put(BurgerDeliveryContract.OrderEntry.COLUMN_DATE, date.getTime());
        return contentValues;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!OrderModel.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final OrderModel other = (OrderModel) obj;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.id;
        return hash;
    }

    public float getTotalValue() {
        float totalValue = 0;
        if (itemList != null) {
            for (OrderItemModel orderItemModel: itemList) {
                totalValue += orderItemModel.getTotal();
            }
        }

        return totalValue;
    }

    public void removeItemFromOrder(int position) {
        itemList.remove(position);
    }

    public CharSequence getStatusDescription(Context context) {
        int statusStringResId = R.string.pending;
        switch (status) {
            case OrderStatus.SENT:
                statusStringResId = R.string.sent;
                break;
            case OrderStatus.PREPARING:
                statusStringResId = R.string.preparing;
                break;
            case OrderStatus.IN_ROUTE:
                statusStringResId = R.string.order_status_in_route;
                break;
            case OrderStatus.DELIVERED:
                statusStringResId = R.string.order_status_delivered;
                break;
        }

        String statusDescription = context.getString(statusStringResId);
        Timber.d("Status description: " + statusDescription);

        return statusDescription;
    }

    public void setOrderItemList(List<OrderItemModel> orderItemList) {
        this.itemList = orderItemList;
    }

    public int getStatus() {
        return status;
    }

    public int getServerId() {
        return serverId;
    }
}
