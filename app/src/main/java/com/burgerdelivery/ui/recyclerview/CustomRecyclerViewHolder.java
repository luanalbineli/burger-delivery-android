package com.burgerdelivery.ui.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {
    public CustomRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    protected Context getContext() {
        return itemView.getContext();
    }
}
