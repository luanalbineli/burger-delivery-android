package com.burgerdelivery.repository;

import android.content.ContentResolver;
import android.net.Uri;

import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.model.OrderItemModel;
import com.burgerdelivery.repository.contentprovider.BurgerDeliveryContract;

import java.sql.SQLDataException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
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

    private Completable observeOnMainThread(Completable completable) {
        return completable.observeOn(AndroidSchedulers.mainThread());
    }
}
