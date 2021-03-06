package com.burgerdelivery.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.burgerdelivery.enunn.AdditionalItemStatus;
import com.burgerdelivery.repository.contentprovider.BurgerDeliveryContract;
import com.burgerdelivery.util.BitFlag;
import com.burgerdelivery.util.SQLUtil;

public class OrderItemModel implements Parcelable {
    private final BurgerModel burgerModel;
    private final int orderId;
    private final int additional;
    private final String observation;
    private int quantity;
    private int id;

    public OrderItemModel(int orderId, int additional, String observation, int quantity, BurgerModel burgerModel) {
        this.burgerModel = burgerModel;
        this.orderId = orderId;
        this.additional = additional;
        this.observation = observation;
        this.quantity = quantity;
    }

    private OrderItemModel(int id, int orderId, int additional, String observation, int quantity, BurgerModel burgerModel) {
        this(orderId, additional, observation, quantity, burgerModel);
        this.id = id;
    }

    protected OrderItemModel(Parcel in) {
        burgerModel = in.readParcelable(BurgerModel.class.getClassLoader());
        orderId = in.readInt();
        additional = in.readInt();
        observation = in.readString();
        quantity = in.readInt();
        id = in.readInt();
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
        return new OrderItemModel(
                SQLUtil.getInt(cursor, BurgerDeliveryContract.OrderItemEntry._ID),
                orderId,
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

    public int getQuantity() {
        return quantity;
    }

    public BurgerModel getBurgerModel() {
        return burgerModel;
    }

    public int getAdditional() {
        return additional;
    }

    public float getSubtotalValue() {
        return (float) this.quantity * this.burgerModel.getPrice();
    }

    public float getAdditionalValue() {
        if (additional == 0) {
            return 0;
        }

        BitFlag bitFlag = new BitFlag(additional);
        float additionalValue = 0;
        if (bitFlag.isSet(AdditionalItemStatus.BURGER)) {
            additionalValue += 4;
        }

        if (bitFlag.isSet(AdditionalItemStatus.CHEDDAR)) {
            additionalValue += 2.5f;
        }

        if (bitFlag.isSet(AdditionalItemStatus.PICKLES)) {
            additionalValue += 1.25f;
        }

        return additionalValue * (float) quantity;
    }

    public float getTotal() {
        return getSubtotalValue() + getAdditionalValue();
    }

    public int getId() {
        return id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(burgerModel, i);
        parcel.writeInt(orderId);
        parcel.writeInt(additional);
        parcel.writeString(observation);
        parcel.writeInt(quantity);
        parcel.writeInt(id);
    }

    public static final Creator<OrderItemModel> CREATOR = new Creator<OrderItemModel>() {
        @Override
        public OrderItemModel createFromParcel(Parcel in) {
            return new OrderItemModel(in);
        }

        @Override
        public OrderItemModel[] newArray(int size) {
            return new OrderItemModel[size];
        }
    };
}
