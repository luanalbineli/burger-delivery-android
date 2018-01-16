package com.burgerdelivery.model;

import android.content.ContentValues;

import com.burgerdelivery.repository.contentprovider.BurgerDeliveryContract;

public class OrderItemModel {
    private final BurgerModel burgerModel;
    private final int orderId;
    private final int additional;
    private final String observation;

    public OrderItemModel(int orderId, int additional, String observation, BurgerModel burgerModel) {
        this.burgerModel = burgerModel;
        this.orderId = orderId;
        this.additional = additional;
        this.observation = observation;
    }

    public ContentValues getContentValues() {
        ContentValues contentProvider = new ContentValues();
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_ORDER_ID, orderId);
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_ADDITIONAL, additional);
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_OBSERVATION, observation);

        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_ID, burgerModel.getId());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_NAME, burgerModel.getName());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_DESCRIPTION, burgerModel.getDescription());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_PRICE, burgerModel.getPrice());
        contentProvider.put(BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_IMAGE_URL, burgerModel.getImageUrl());
        return contentProvider;
    }
}
