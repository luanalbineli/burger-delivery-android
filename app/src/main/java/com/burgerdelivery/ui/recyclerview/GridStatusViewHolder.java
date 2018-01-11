package com.burgerdelivery.ui.recyclerview;

import android.support.annotation.StringRes;
import android.view.View;

import com.burgerdelivery.R;
import com.burgerdelivery.enunn.RequestStatus;
import com.burgerdelivery.ui.RequestStatusView;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class GridStatusViewHolder extends CustomRecyclerViewHolder {
    @BindView(R.id.rsvRequestStatus)
    RequestStatusView mRequestStatusView;

    public GridStatusViewHolder(View itemView, @StringRes int emptyMessageResId, RequestStatusView.ITryAgainListener tryAgainClick) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        mRequestStatusView.setEmptyMessage(emptyMessageResId);
        mRequestStatusView.setTryAgainClickListener(tryAgainClick);
    }

    void bind(@RequestStatus int requestStatus, int numberOfItems) {
        mRequestStatusView.setRequestStatus(requestStatus, numberOfItems == 0);
    }
}
