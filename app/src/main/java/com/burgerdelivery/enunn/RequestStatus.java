package com.burgerdelivery.enunn;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({RequestStatus.LOADING, RequestStatus.ERROR, RequestStatus.EMPTY, RequestStatus.HIDDEN})
@Retention(RetentionPolicy.SOURCE)
public @interface RequestStatus {
    int LOADING = 0;
    int ERROR = 1;
    int EMPTY = 2;
    int HIDDEN = 3;
}
