package com.burgerdelivery.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BurgerModel implements Parcelable {
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

    @SerializedName("imageUrl")
    private String imageUrl;

    public BurgerModel(int id, String name, String description, String[] ingredients, float price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    protected BurgerModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        ingredients = in.createStringArray();
        price = in.readFloat();
        imageUrl = in.readString();
    }

    public static final Creator<BurgerModel> CREATOR = new Creator<BurgerModel>() {
        @Override
        public BurgerModel createFromParcel(Parcel in) {
            return new BurgerModel(in);
        }

        @Override
        public BurgerModel[] newArray(int size) {
            return new BurgerModel[size];
        }
    };

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

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeStringArray(ingredients);
        dest.writeFloat(price);
        dest.writeString(imageUrl);
    }
}
