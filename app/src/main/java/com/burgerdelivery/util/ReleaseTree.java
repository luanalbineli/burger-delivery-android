package com.burgerdelivery.util;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import timber.log.Timber;

public class ReleaseTree extends Timber.Tree {

    @Override
    protected void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return;
        }

        if (t == null) {
            FirebaseCrash.report(new Exception(message));
        } else {
            FirebaseCrash.report(t);
        }
    }
}
