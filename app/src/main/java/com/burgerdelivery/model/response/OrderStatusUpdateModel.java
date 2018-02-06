package com.burgerdelivery.model.response;

import com.google.gson.annotations.SerializedName;

public class OrderStatusUpdateModel {
    @SerializedName("id")
    private int id;

    @SerializedName("status")
    private int status;

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }
}
