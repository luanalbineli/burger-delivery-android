package com.burgerdelivery.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public class RequestStatusView extends FrameLayout {
    public RequestStatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initializeViews();
    }

    private void initializeViews() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.request_status, this);
        }
    }
}
