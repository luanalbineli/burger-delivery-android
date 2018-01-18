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

    private List<OrderItemModel> mItemList;
    private Date mDate;
    private int mStatus;
    private int mId;

    public OrderModel(@OrderStatus int status, Date date) {
        mStatus = status;
        mDate = date;
    }

    private OrderModel(int id, @OrderStatus int status, Date date) {
        this(status, date);

        mId = id;
    }

    private OrderModel(int id) {
        mId = id;
    }

    public void addItemToOrder(OrderItemModel orderItemModel) {
        preCheckItemList();
        mItemList.add(orderItemModel);
    }

    public List<OrderItemModel> getItemList() {
        preCheckItemList();
        return mItemList;
    }

    private void preCheckItemList() {
        if (mItemList == null) {
            mItemList = new ArrayList<>();
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
        contentValues.put(BurgerDeliveryContract.OrderEntry.COLUMN_STATUS, mStatus);
        contentValues.put(BurgerDeliveryContract.OrderEntry.COLUMN_DATE, mDate.getTime());
        return contentValues;
    }

    public int getId() {
        return mId;
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
        hash = 53 * hash + this.mId;
        return hash;
    }
}
