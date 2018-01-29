package com.burgerdelivery.model.response;

import com.google.gson.annotations.SerializedName;

public class FinishOrderResponseModel {
    @SerializedName("orderId")
    private int orderId;

    public int getOrderId() {
        return orderId;
    }
}
