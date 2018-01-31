package com.burgerdelivery.repository.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.security.InvalidParameterException;
import java.util.Arrays;

import timber.log.Timber;


public class BurgerProvider extends ContentProvider {
    private static final int CODE_ORDER = 101;

    private static final int CODE_ORDER_ITEMS = 103;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(BurgerDeliveryContract.CONTENT_AUTHORITY, BurgerDeliveryContract.PATH_ORDER, CODE_ORDER);
        URI_MATCHER.addURI(BurgerDeliveryContract.CONTENT_AUTHORITY, BurgerDeliveryContract.PATH_ITEM, CODE_ORDER_ITEMS);
    }

    private OrderDatabase mMovieDatabase;

    @Override
    public boolean onCreate() {
        mMovieDatabase = new OrderDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sort) {
        final int code = URI_MATCHER.match(uri);
        if (code == UriMatcher.NO_MATCH) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase sqLiteDatabase = mMovieDatabase.getReadableDatabase();

        String tableName = code == CODE_ORDER ?
                BurgerDeliveryContract.OrderEntry.TABLE_NAME :
                BurgerDeliveryContract.OrderItemEntry.TABLE_NAME;

        final Cursor cursor = sqLiteDatabase.query(tableName, columns, selection, selectionArgs, null, null, sort);

        final Context context = getContext();
        if (context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long createdId;
        switch (URI_MATCHER.match(uri)) {
            case CODE_ORDER:
                SQLiteDatabase sqLiteDatabase = getWritableDatabase();
                if (sqLiteDatabase == null) {
                    return null;
                }

                createdId = sqLiteDatabase.insertOrThrow(BurgerDeliveryContract.OrderEntry.TABLE_NAME, null, contentValues);
                break;
            case CODE_ORDER_ITEMS:
                SQLiteDatabase sqLiteDatabaseOrderItem = getWritableDatabase();
                if (sqLiteDatabaseOrderItem == null) {
                    return null;
                }

                createdId = sqLiteDatabaseOrderItem.insertOrThrow(BurgerDeliveryContract.OrderItemEntry.TABLE_NAME, null, contentValues);
                break;
            default:
                throw new InvalidParameterException("Unknown uri: " + uri);
        }

        if (createdId <= 0) {
            throw new SQLException("Failed to insert a record into " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, createdId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] selectionArgs) {
        switch (URI_MATCHER.match(uri)) {
            case CODE_ORDER:
                throw new IllegalArgumentException("You can only remove items. The order can't be removed");
            case CODE_ORDER_ITEMS:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }

                final SQLiteDatabase sqLiteDatabase = mMovieDatabase.getWritableDatabase();

                final int count = sqLiteDatabase.delete(BurgerDeliveryContract.OrderItemEntry.TABLE_NAME, where, selectionArgs);

                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String where, @Nullable String[] selectionArgs) {
        switch (URI_MATCHER.match(uri)) {
            case CODE_ORDER:
                Timber.d("Updating the order.\nWHERE: " + where + "\nselectionArgs: " + formatSelectionArgs(selectionArgs) + formatContentValues(contentValues));
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }

                final SQLiteDatabase sqLiteDatabase = mMovieDatabase.getWritableDatabase();
                final int count = sqLiteDatabase.update(BurgerDeliveryContract.OrderEntry.TABLE_NAME, contentValues, where, selectionArgs);
                if (count == 0) {
                    Timber.e("An error occurred while tried to updated the order (0 order updated)");
                    throw new SQLException("Failed to update the order " + uri);
                }

                context.getContentResolver().notifyChange(uri, null);
                return count;
            case CODE_ORDER_ITEMS:
                throw new IllegalArgumentException("You can't update order items");
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private String formatContentValues(ContentValues contentValues) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key: contentValues.keySet()) {
            stringBuilder.append("\nKEY: ")
                    .append(key)
                    .append(" | VALUE: ")
                    .append(contentValues.get(key));
        }
        return stringBuilder.toString();
    }

    private String formatSelectionArgs(String[] selectionArgs) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String content: selectionArgs) {
            stringBuilder.append(content);
        }
        return stringBuilder.toString();
    }

    private @Nullable SQLiteDatabase getWritableDatabase() {
        final Context context = getContext();
        if (context == null) {
            return null;
        }

        return mMovieDatabase.getWritableDatabase();
    }
}
