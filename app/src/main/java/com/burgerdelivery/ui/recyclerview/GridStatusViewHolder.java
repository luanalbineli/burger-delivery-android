package com.burgerdelivery.ui.recyclerview;

import android.view.View;

import com.burgerdelivery.enunn.RequestStatus;
import com.burgerdelivery.ui.RequestStatusView;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by luan.pinto on 11/01/2018.
 */

public class GridStatusViewHolder extends CustomRecyclerViewHolder {
    @BindView(R.id.rsvRequestStatus)
    RequestStatusView mRequestStatusView;

    public GridStatusViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        mRequestStatusView.setEmptyMessage(emptyMessageResId)
        mRequestStatusView.setTryAgainClickListener(tryAgainClick)
    }

    fun bind(@RequestStatusDescriptor.RequestStatus requestStatus: Int, numberOfItems: Int) {
        itemView.rsvRequestStatus.setRequestStatus(requestStatus, numberOfItems == 0)
        Timber.i("REDRAWING GRID STATUS: " + requestStatus)
    }
}
