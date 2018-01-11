package com.burgerdelivery.model;


import com.google.gson.annotations.SerializedName;

public class BurgerModel {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("ingredients")
    private String[] ingredients;

    @SerializedName("price")
    private float price;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public float getPrice() {
        return price;
    }
}
