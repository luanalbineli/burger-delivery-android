package com.burgerdelivery.repository.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class OrderDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "burgerdelivery";
    private static final int DATABASE_VERSION = 1;

    OrderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_ORDER = "CREATE TABLE " + BurgerDeliveryContract.OrderEntry.TABLE_NAME + " (" +
                BurgerDeliveryContract.OrderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BurgerDeliveryContract.OrderEntry.COLUMN_STATUS + " INTEGER NOT NULL, " +
                BurgerDeliveryContract.OrderEntry.COLUMN_DATE + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TABLE_ORDER);

        final String CREATE_TABLE_ORDER_ITEM = "CREATE TABLE " + BurgerDeliveryContract.OrderItemEntry.TABLE_NAME + " (" +
                BurgerDeliveryContract.OrderItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BurgerDeliveryContract.OrderItemEntry.COLUMN_ORDER_ID + " INTEGER NOT NULL, " +
                BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_ID + " INTEGER NOT NULL, " +
                BurgerDeliveryContract.OrderItemEntry.COLUMN_ADDITIONAL + " INTEGER NOT NULL, " +
                BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_PRICE + " REAL NOT NULL, " +
                BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_NAME + " TEXT NOT NULL, " +
                BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_DESCRIPTION + " TEXT NOT NULL, " +
                BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_IMAGE_URL + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + BurgerDeliveryContract.OrderItemEntry.COLUMN_BURGER_ID + ")" +
                    " REFERENCES "
                + BurgerDeliveryContract.OrderEntry.TABLE_NAME + "(" + BurgerDeliveryContract.OrderEntry._ID + "));";

        db.execSQL(CREATE_TABLE_ORDER_ITEM);
    }

    @Override
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
