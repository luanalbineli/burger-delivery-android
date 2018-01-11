package com.burgerdelivery.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.burgerdelivery.R;
import com.burgerdelivery.enunn.RequestStatus;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

public class RequestStatusView extends FrameLayout {
    @RequestStatus
    private int mRequestStatus = RequestStatus.EMPTY;

    private @Nullable ITryAgainListener mTryAgainClickListener;

    @BindView(R.id.tvRequestStatusEmptyMessage)
    TextView mEmptyMessageTextView;

    @BindView(R.id.pbRequestStatusLoading)
    ProgressBar mProgressBar;

    @BindView(R.id.clRequestStatusError)
    ConstraintLayout mErrorContainer;

    @BindView(R.id.btRequestStatusRetry)
    Button mRetryButton;

    public RequestStatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initializeViews();
    }

    private void initializeViews() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View childView = inflater.inflate(R.layout.request_status, this);
            ButterKnife.bind(this, childView);
        }
    }

    void setRequestStatus(int requestStatus) {
        setRequestStatus(requestStatus, false);
    }

    public void setRequestStatus(@RequestStatus int requestStatus, boolean matchParentHeight) {
        this.mRequestStatus = requestStatus;
        redrawStatus(matchParentHeight);
    }

    public void setEmptyMessage(@StringRes int messageResId) {
        mEmptyMessageTextView.setText(messageResId);
    }

    private void redrawStatus(boolean matchParentHeight) {
        toggleStatus(mRequestStatus == RequestStatus.LOADING, mRequestStatus == RequestStatus.ERROR, mRequestStatus == RequestStatus.EMPTY, matchParentHeight ? MATCH_PARENT : WRAP_CONTENT);
    }

    private void toggleStatus(boolean loadingVisible, boolean errorVisible, boolean emptyMessageVisible, int viewHeight) {
        mProgressBar.setVisibility(loadingVisible ? View.VISIBLE : View.INVISIBLE);
        mErrorContainer.setVisibility(errorVisible ? View.VISIBLE : View.INVISIBLE);
        mEmptyMessageTextView.setVisibility(emptyMessageVisible ? View.VISIBLE : View.INVISIBLE);

        mRetryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTryAgainClickListener != null) {
                    mTryAgainClickListener.tryAgain();
                }
            }
        });

        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height = viewHeight;
        this.setLayoutParams(layoutParams);
    }

    public void setTryAgainClickListener(ITryAgainListener tryAgainClick) {
        mTryAgainClickListener = tryAgainClick;
    }

    public interface ITryAgainListener {
        void tryAgain();
    }
}
