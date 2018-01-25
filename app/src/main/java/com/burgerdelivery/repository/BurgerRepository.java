package com.burgerdelivery.repository;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.enunn.OrderStatus;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.repository.contentprovider.BurgerDeliveryContract;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class BurgerRepository {
    private final BurgerDeliveryApplication mBurgerDeliveryApplication;

    @Inject
    public BurgerRepository(BurgerDeliveryApplication burgerDeliveryApplication) {
        this.mBurgerDeliveryApplication = burgerDeliveryApplication;
    }

    public Single<Integer> insertBurgerItemIntoOrder(final OrderItemModel orderItemModel) {
        return observeOnMainThread(Single.create(singleEmitter -> {
            ContentResolver contentResolver = mBurgerDeliveryApplication.getContentResolver();
            Uri uri = contentResolver.insert(BurgerDeliveryContract.OrderItemEntry.CONTENT_URI, orderItemModel.getContentValues());

            Timber.d("Result of the insertion: " + uri);
            if (uri == null) {
                singleEmitter.onError(new SQLDataException("An internal error occurred."));
                return;
            }
            singleEmitter.onSuccess(Integer.valueOf(uri.getLastPathSegment()));
        }));
    }

    public Single<OrderModel> getCurrentPendingOrder() {
        return observeOnMainThread(Single.create((SingleOnSubscribe<OrderModel>) singleEmitter -> {
            ContentResolver contentResolver = mBurgerDeliveryApplication.getContentResolver();
            String selection = BurgerDeliveryContract.OrderEntry.COLUMN_STATUS + " = ?";
            String[] selectionArgs = new String[]{String.valueOf(OrderStatus.PENDING)};
            Cursor cursor = contentResolver.query(BurgerDeliveryContract.OrderEntry.CONTENT_URI, null, selection, selectionArgs, null);
            if (cursor == null) {
                singleEmitter.onError(new SQLDataException("An internal error occurred"));
                return;
            }

            if (cursor.moveToNext()) {
                singleEmitter.onSuccess(OrderModel.fromCursor(cursor));
            } else {
                singleEmitter.onSuccess(OrderModel.EMPTY);
            }
        }));
    }

    public Single<List<OrderItemModel>> getItemsByOrderId(final int orderId) {
        return observeOnMainThread(Single.create((SingleOnSubscribe<List<OrderItemModel>>) singleEmitter -> {
            ContentResolver contentResolver = mBurgerDeliveryApplication.getContentResolver();
            String selection = BurgerDeliveryContract.OrderItemEntry.COLUMN_ORDER_ID + " = ?";
            String[] selectionArgs = new String[]{String.valueOf(orderId)};
            Cursor cursor = contentResolver.query(BurgerDeliveryContract.OrderItemEntry.CONTENT_URI, null, selection, selectionArgs, null);
            if (cursor == null) {
                singleEmitter.onError(new SQLDataException("An internal error occurred"));
                return;
            }

            List<OrderItemModel> orderItemList = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                orderItemList.add(OrderItemModel.fromCursor(orderId, cursor));
            }

            singleEmitter.onSuccess(orderItemList);
        }));
    }

    public Single<Integer> insertOrder(final OrderModel orderModel) {
        return observeOnMainThread(Single.create((SingleOnSubscribe<Integer>) singleEmitter -> {
            ContentResolver contentResolver = mBurgerDeliveryApplication.getContentResolver();
            Uri uri = contentResolver.insert(BurgerDeliveryContract.OrderEntry.CONTENT_URI, orderModel.toContentValues());
            if (uri == null) {
                singleEmitter.onError(new SQLDataException("An internal error occurred"));
                return;
            }

            singleEmitter.onSuccess(Integer.parseInt(uri.getLastPathSegment()));
        }));
    }

    public Completable removeOrderItem(int orderItemId) {
        return observeOnMainThread(Completable.create(emitter -> {
            ContentResolver contentResolver = mBurgerDeliveryApplication.getContentResolver();
            String where = BurgerDeliveryContract.OrderItemEntry._ID + " = ?";
            String[] selectionArgs = new String[] { String.valueOf(orderItemId) };
            int numberOfRemovedItems = contentResolver.delete(BurgerDeliveryContract.OrderItemEntry.CONTENT_URI, where, selectionArgs);
            if (numberOfRemovedItems == 0) {
                emitter.onError(new SQLDataException("An internal error occurred"));
                return;
            }

            emitter.onComplete();
        }));
    }

    private Completable observeOnMainThread(Completable completable) {
        return completable.observeOn(AndroidSchedulers.mainThread());
    }

    private <T> Single<T> observeOnMainThread(Single<T> single) {
        return single.observeOn(AndroidSchedulers.mainThread());
    }
}
