package com.burgerdelivery.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.burgerdelivery.repository.contentprovider.BurgerDeliveryContract;
import com.burgerdelivery.util.SQLUtil;

public class OrderItemModel {
    private final BurgerModel burgerModel;
    private final int orderId;
    private final int additional;
    private final String observation;
    private final int quantity;

    public OrderItemModel(int orderId, int additional, String observation, int quantity, BurgerModel burgerModel) {
        this.burgerModel = burgerModel;
        this.orderId = orderId;
        this.additional = additional;
        this.observation = observation;
        this.quantity = quantity;
    }

    public ContentValues getContentValues() {
        ContentValues contentProvider = new ContentValues();
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_ORDER_ID, orderId);
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_ADDITIONAL, additional);
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_OBSERVATION, observation);
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_QUANTITY, quantity);

        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_ID, burgerModel.getId());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_NAME, burgerModel.getName());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_DESCRIPTION, burgerModel.getDescription());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_PRICE, burgerModel.getPrice());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_IMAGE_URL, burgerModel.getImageUrl());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_INGREDIENTS, SQLUtil.stringArrayToString(burgerModel.getIngredients()));

        return contentProvider;
    }

    public static OrderItemModel fromCursor(int orderId, Cursor cursor) {
        return new OrderItemModel(orderId,
                SQLUtil.getInt(cursor, BurgerDeliveryContract.OrderItemEntry.COLUMN_ADDITIONAL),
                SQLUtil.getString(cursor, BurgerDeliveryContract.OrderItemEntry.COLUMN_OBSERVATION),
                SQLUtil.getInt(cursor, BurgerDeliveryContract.OrderItemEntry.COLUMN_QUANTITY),
                new BurgerModel(
                        SQLUtil.getInt(cursor, BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_ID),
                        SQLUtil.getString(cursor, BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_NAME),
                        SQLUtil.getString(cursor, BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_DESCRIPTION),
                        SQLUtil.getStringArray(cursor, BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_INGREDIENTS),
                        SQLUtil.getFloat(cursor, BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_PRICE),
                        SQLUtil.getString(cursor, BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_IMAGE_URL)
                ));
    }
}
