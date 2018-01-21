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

    public static String getString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getString(columnIndex);
    }

    public static String stringArrayToString(String[] values) {
        StringBuilder builder = new StringBuilder();
        for (String value : values) {
            builder.append(value)
                    .append(SEPARATOR);
        }
        return builder.toString();
    }

    public static String[] getStringArray(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        String content = cursor.getString(columnIndex);

        return content.split(SEPARATOR);
    }

    private static final String SEPARATOR = "__,__";

    public static float getFloat(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getFloat(columnIndex);
    }
}
