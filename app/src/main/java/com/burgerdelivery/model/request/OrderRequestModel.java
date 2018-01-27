package com.burgerdelivery.model.request;

import com.burgerdelivery.model.OrderModel;

public class OrderRequestModel {
    private final String fcmToken;
    private final OrderModel orderModel;

    public OrderRequestModel(OrderModel orderModel, String fcmToken) {
        this.orderModel = orderModel;
        this.fcmToken = fcmToken;
    }
}
