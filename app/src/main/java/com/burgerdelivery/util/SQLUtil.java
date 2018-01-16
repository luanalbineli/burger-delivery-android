package com.burgerdelivery.util;

import android.database.Cursor;

import java.util.Date;

public abstract class SQLUtil {
    public static Date getDate(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return new Date(cursor.getLong(columnIndex));
    }

    public static int getInt(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getInt(columnIndex);
    }
}
