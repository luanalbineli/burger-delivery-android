package com.burgerdelivery.model.response;

import com.google.gson.annotations.SerializedName;

public class FinishOrderResponseModel {
    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }
}
