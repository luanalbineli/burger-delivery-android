package com.burgerdelivery.repository;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.enunn.OrderStatus;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.repository.contentprovider.BurgerDeliveryContract;

import java.sql.SQLDataException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class BurgerRepository {
    private final BurgerDeliveryApplication mBurgerDeliveryApplication;

    @Inject
    public BurgerRepository(BurgerDeliveryApplication burgerDeliveryApplication) {
        this.mBurgerDeliveryApplication = burgerDeliveryApplication;
    }

    public Completable insertBurgerItemIntoOrder(final OrderItemModel orderItemModel) {
        return observeOnMainThread(Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter completableEmitter) throws Exception {
                ContentResolver contentResolver = mBurgerDeliveryApplication.getContentResolver();
                Uri uri = contentResolver.insert(BurgerDeliveryContract.OrderItemEntry.CONTENT_URI, orderItemModel.getContentValues());

                Timber.d("Result of the insertion: " + uri);
                if (uri == null) {
                    completableEmitter.onError(new SQLDataException("An internal error occurred."));
                    return;
                }

                completableEmitter.onComplete();
            }
        }));
    }

    public Single<OrderModel> getCurrentPendingOrder() {
        return observeOnMainThread(Single.create(new SingleOnSubscribe<OrderModel>() {
            @Override
            public void subscribe(SingleEmitter<OrderModel> singleEmitter) throws Exception {
                ContentResolver contentResolver = mBurgerDeliveryApplication.getContentResolver();
                String selection = BurgerDeliveryContract.OrderEntry.COLUMN_STATUS + " = ?";
                String[] selectionArgs = new String[]{String.valueOf(OrderStatus.PENDING)};
                Cursor cursor = contentResolver.query(BurgerDeliveryContract.OrderEntry.CONTENT_URI, null, selection, selectionArgs, null);
                if (cursor == null) {
                    singleEmitter.onError(new SQLDataException("An internal error occurred"));
                    return;
                }

                if (cursor.getCount() == 0) {
                    singleEmitter.onSuccess(null);
                } else {
                    singleEmitter.onSuccess(OrderModel.fromCursor(cursor));
                }
            }
        }));
    }

    public Single<Integer> insertOrder(final OrderModel orderModel) {
        return observeOnMainThread(Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> singleEmitter) throws Exception {
                ContentResolver contentResolver = mBurgerDeliveryApplication.getContentResolver();
                Uri uri = contentResolver.insert(BurgerDeliveryContract.OrderEntry.CONTENT_URI, orderModel.toContentValues());
                if (uri == null) {
                    singleEmitter.onError(new SQLDataException("An internal error occurred"));
                    return;
                }

                singleEmitter.onSuccess(Integer.parseInt(uri.getLastPathSegment()));
            }
        }));
    }

    private Completable observeOnMainThread(Completable completable) {
        return completable.observeOn(AndroidSchedulers.mainThread());
    }

    private <T> Single<T> observeOnMainThread(Single<T> single) {
        return single.observeOn(AndroidSchedulers.mainThread());
    }
}
