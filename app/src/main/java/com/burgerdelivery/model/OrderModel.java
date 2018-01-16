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
    private List<OrderItemModel> mItemList;
    private Date mDate;
    private int mStatus;
    private Integer mId;

    public OrderModel(@OrderStatus int status, Date date) {
        mStatus = status;
        mDate = date;
    }

    private OrderModel(int id, @OrderStatus int status, Date date) {
        this(status, date);

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

    public Integer getId() {
        return mId;
    }
}
