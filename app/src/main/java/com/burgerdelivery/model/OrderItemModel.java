package com.burgerdelivery.model;

import android.content.ContentProvider;
import android.content.ContentValues;

import com.burgerdelivery.repository.contentprovider.BurgerDeliveryContract;

public class OrderItemModel {
    private final BurgerModel burgerModel;
    private final int orderId;

    public OrderItemModel(int orderId, BurgerModel burgerModel) {
        this.burgerModel = burgerModel;
        this.orderId = orderId;
    }

    public ContentValues getContentValues() {
        ContentValues contentProvider = new ContentValues();
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_ID, burgerModel.getId());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_NAME, burgerModel.getName());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_DESCRIPTION, burgerModel.getDescription());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_PRICE, burgerModel.getPrice());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_IMAGE_URL, burgerModel.getImageUrl());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_ADDITIONAL, burgerModel.getId());
        return contentProvider;
    }
}
