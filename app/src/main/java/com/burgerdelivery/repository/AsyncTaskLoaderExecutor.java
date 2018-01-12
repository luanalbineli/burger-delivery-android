package com.burgerdelivery.repository;

import android.content.AsyncTaskLoader;
import android.content.Context;

import timber.log.Timber;

public abstract class AsyncTaskLoaderExecutor<T> extends AsyncTaskLoader<T> {
    private T mData;
    private boolean hasResult = false;

    public AsyncTaskLoaderExecutor(final Context context) {
        super(context);
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        Timber.d("onStartLoading");
        if (takeContentChanged()) {
            Timber.d("Forcing the loading");
            forceLoad();
        } else if (hasResult) {
            Timber.d("Already have the result. Sending it.");
            deliverResult(mData);
        }
    }

    @Override
    public void deliverResult(final T data) {
        Timber.d("deliverResult - data: " + data);
        mData = data;
        hasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (hasResult) {
            mData = null;
            hasResult = false;
        }
    }
}
