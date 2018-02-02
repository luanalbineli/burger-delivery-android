package com.burgerdelivery.repository;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.enunn.OrderStatus;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.model.OrderModel;
import com.burgerdelivery.model.request.OrderRequestModel;
import com.burgerdelivery.model.response.FinishOrderResponseModel;
import com.burgerdelivery.repository.contentprovider.BurgerDeliveryContract;
import com.google.firebase.iid.FirebaseInstanceId;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Retrofit;
import timber.log.Timber;

public class BurgerRepository {
    private final BurgerDeliveryApplication mBurgerDeliveryApplication;
    private final Retrofit mRetrofit;

    @Inject
    public BurgerRepository(BurgerDeliveryApplication burgerDeliveryApplication, Retrofit retrofit) {
        this.mBurgerDeliveryApplication = burgerDeliveryApplication;
        this.mRetrofit = retrofit;
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
            List<OrderItemModel> orderItemList = getItemsByOrderId(contentResolver, orderId);
            if (orderItemList == null) {
                singleEmitter.onError(new SQLDataException("An internal error occurred"));
            } else {
                singleEmitter.onSuccess(orderItemList);
            }
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
            String[] selectionArgs = new String[]{String.valueOf(orderItemId)};
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

    public Single<FinishOrderResponseModel> finishOrder(OrderModel orderModel) {
        OrderRequestModel orderRequestModel = new OrderRequestModel(
                orderModel,
                FirebaseInstanceId.getInstance().getToken());

        return observeOnMainThread(mRetrofit.create(APIInterface.class).finishOrder(orderRequestModel));
    }

    public Completable updateSentOrderById(int orderId, int serverId) {
        Timber.d("Updating the order " + orderId + " to serverId: " + serverId + " and status SENT.");
        ContentValues contentValues = new ContentValues();
        contentValues.put(BurgerDeliveryContract.OrderEntry.COLUMN_STATUS, OrderStatus.SENT);
        contentValues.put(BurgerDeliveryContract.OrderEntry.COLUMN_SERVER_ID, serverId);

        return observeOnMainThread(Completable.create(emitter -> {
            ContentResolver contentResolver = mBurgerDeliveryApplication.getContentResolver();
            String where = BurgerDeliveryContract.OrderEntry._ID + " = ?";
            String[] selectionArgs = new String[]{String.valueOf(orderId)};
            int numberOfUpdatedItems = contentResolver.update(BurgerDeliveryContract.OrderEntry.CONTENT_URI, contentValues, where, selectionArgs);
            if (numberOfUpdatedItems <= 0) {
                Timber.e("Error updating the order " + orderId + " to serverId: " + serverId + " and status SENT. THE ORDER WAS NOT FOUND (0 items updated).");
                emitter.onError(new SQLException("An error occurred while tried to update the order: " + orderId + ". No order was updated by this id."));
                return;
            }

            Timber.d("Successfully updated the order " + orderId + " to serverId: " + serverId + " and status SENT.");
            emitter.onComplete();
        }));
    }

    public Completable updateOrderStatusByServerId(int serverId, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BurgerDeliveryContract.OrderEntry.COLUMN_STATUS, status);

        return observeOnMainThread(Completable.create(emitter -> {
            ContentResolver contentResolver = mBurgerDeliveryApplication.getContentResolver();
            String where = BurgerDeliveryContract.OrderEntry.COLUMN_SERVER_ID + " = ?";
            String[] selectionArgs = new String[]{String.valueOf(serverId)};
            contentResolver.update(BurgerDeliveryContract.OrderEntry.CONTENT_URI, contentValues, where, selectionArgs);

            emitter.onComplete();
        }));
    }

    public Single<OrderModel> getLastOrder() {
        return observeOnMainThread(Single.create((SingleOnSubscribe<OrderModel>) singleEmitter -> {
            ContentResolver contentResolver = mBurgerDeliveryApplication.getContentResolver();
            String orderBy = BurgerDeliveryContract.OrderEntry.COLUMN_DATE + " DESC";

            Cursor cursor = contentResolver.query(BurgerDeliveryContract.OrderEntry.CONTENT_URI, null, null, null, orderBy);
            if (cursor == null) {
                singleEmitter.onError(new SQLDataException("An internal error occurred"));
                return;
            }

            if (!cursor.moveToNext()) {
                singleEmitter.onSuccess(OrderModel.EMPTY);
                return;
            }

            OrderModel orderModel = OrderModel.fromCursor(cursor);
            List<OrderItemModel> orderItemList = getItemsByOrderId(contentResolver, orderModel.getId());
            if (orderItemList == null) {
                singleEmitter.onError(new SQLDataException("An internal error occurred"));
                return;
            }

            orderModel.setOrderItemList(orderItemList);
            singleEmitter.onSuccess(orderModel);
        }));
    }

    private @Nullable
    List<OrderItemModel> getItemsByOrderId(ContentResolver contentResolver, int orderId) {
        String selection = BurgerDeliveryContract.OrderItemEntry.COLUMN_ORDER_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(orderId)};

        Cursor cursor = contentResolver.query(BurgerDeliveryContract.OrderItemEntry.CONTENT_URI, null, selection, selectionArgs, null);
        if (cursor == null) {
            return null;
        }

        List<OrderItemModel> orderItemList = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            orderItemList.add(OrderItemModel.fromCursor(orderId, cursor));
        }

        return orderItemList;
    }
}
