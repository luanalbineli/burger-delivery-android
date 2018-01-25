package com.burgerdelivery.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.burgerdelivery.enunn.OrderStatus;
import com.burgerdelivery.repository.contentprovider.BurgerDeliveryContract;
import com.burgerdelivery.util.SQLUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderModel {
    public static final OrderModel EMPTY = new OrderModel(Integer.MIN_VALUE);

    private List<OrderItemModel> itemList;
    private Date date;
    private int status;
    private int id;

    private int mServerId;

    public OrderModel(@OrderStatus int status, Date date) {
        this.status = status;
        this.date = date;
    }

    private OrderModel(int id, @OrderStatus int status, Date date) {
        this(status, date);

        this.id = id;
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
                SQLUtil.getDate(cursor, BurgerDeliveryContract.OrderEntry.COLUMN_DATE)
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
}
