package com.burgerdelivery.enunn;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({OrderStatus.PENDING, OrderStatus.SENT, OrderStatus.PREPARING, OrderStatus.IN_ROUTE, OrderStatus.DELIVERED})
@Retention(RetentionPolicy.SOURCE)
public @interface OrderStatus {
    int PENDING = 0;
    int SENT = 1;
    int PREPARING = 2;
    int IN_ROUTE = 3;
    int DELIVERED = 4;
}
