package com.burgerdelivery.ui.recyclerview;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.burgerdelivery.R;
import com.burgerdelivery.enunn.RequestStatus;
import com.burgerdelivery.ui.RequestStatusView;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public abstract class CustomRecyclerViewAdapter<TItem, THolder extends CustomRecyclerViewHolder> extends RecyclerView.Adapter<CustomRecyclerViewHolder> {
    private final List<TItem> mItems;
    //private var mOnItemClickListener: ((position: Int, item: TItem) -> Unit)? = null
    private @RequestStatus int mRequestStatus = RequestStatus.HIDDEN;

    private int mEmptyMessageResId = R.string.the_list_is_empty;

    private @Nullable
    RequestStatusView.ITryAgainListener mTryAgainClickListener;

    private @Nullable IItemClickListener<TItem> mOnItemClickListener;

    protected CustomRecyclerViewAdapter() {
        this(new ArrayList<TItem>());
    }

    private CustomRecyclerViewAdapter(List<TItem> items) {
        this.mItems = items;
    }

    public CustomRecyclerViewAdapter(@StringRes int emptyMessageResId, @Nullable RequestStatusView.ITryAgainListener tryAgainClickListener) {
        this();
        mEmptyMessageResId = emptyMessageResId;
        mTryAgainClickListener = tryAgainClickListener;
    }

    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.GRID_STATUS) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_status, parent, false);
            return new GridStatusViewHolder(itemView, mEmptyMessageResId, mTryAgainClickListener);
        }
        return onCreateItemViewHolder(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    @Override
    public void onBindViewHolder(final CustomRecyclerViewHolder holder, int position) {
        if (holder.getItemViewType() == ViewType.GRID_STATUS) {
            GridStatusViewHolder gridStatusViewHolder = (GridStatusViewHolder) holder;
            gridStatusViewHolder.bind(mRequestStatus, mItems.size());
            return;
        }


        onBindItemViewHolder((THolder) holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.click(holder.getAdapterPosition(), mItems.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    public int getItemCount() {
        return mItems.size() + (mRequestStatus == RequestStatus.HIDDEN ? 0 : 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mItems.size()) {
            return ViewType.GRID_STATUS;
        }

        int itemViewType = getItemViewTypeOverride(position);
        if (itemViewType == ViewType.GRID_STATUS) {
            throw new InvalidParameterException("The view type must be different of ${ViewType.GRID_STATUS}");
        }
        return itemViewType;
    }

    public int getItemViewTypeOverride(int position) {
        return ViewType.ITEM;
    }

    protected TItem getItemByPosition(int position) {
        return mItems.get(position);
    }

    public void addItems(List<TItem> items) {
        hideRequestStatus();

        int itemCount = mItems.size();
        mItems.addAll(items);
        notifyItemRangeInserted(itemCount, items.size());
    }

    void replaceItems(List<TItem> items) {
        redrawGridStatus(RequestStatus.HIDDEN);

        clearItems();

        mItems.addAll(items);
        notifyItemRangeInserted(0, items.size());
    }

    void clearItems() {
        int itemCount = mItems.size();
        if (itemCount > 0) {
            mItems.clear();
            notifyItemRangeRemoved(0, itemCount);
        }
    }

    void removeItemByIndex(int index) {
        mItems.remove(index);
        notifyItemRemoved(index);
    }

    void insertItemByIndex(TItem item, int index) {
        mItems.add(index, item);
        notifyItemInserted(index);
    }

    public void showLoading() {
        redrawGridStatus(RequestStatus.LOADING);
    }

    void hideRequestStatus() {
        redrawGridStatus(RequestStatus.HIDDEN);
    }

    public void hideLoadingIndicator() {
        if (mRequestStatus == RequestStatus.LOADING) { // Hide only if is loading
            hideRequestStatus();
        }
    }

    public void showEmptyMessage() {
        redrawGridStatus(RequestStatus.EMPTY);
    }

    public void showErrorMessage() {
        redrawGridStatus(RequestStatus.ERROR);
    }

    private void redrawGridStatus(int gridStatus) {
        Timber.i("REDRAWING THE GRID STATUS: " + gridStatus);
        int previousRequestStatus = mRequestStatus;
        mRequestStatus = gridStatus;
        if (mRequestStatus == RequestStatus.HIDDEN) {
            notifyItemRemoved(mItems.size());
        } else if (previousRequestStatus == RequestStatus.HIDDEN) {
            notifyItemInserted(mItems.size());
        } else {
            notifyItemChanged(mItems.size());
        }
    }

    protected abstract THolder onCreateItemViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType);

    protected abstract void onBindItemViewHolder(THolder holder, int position);

    @interface ViewType {
        int GRID_STATUS = 0;
        int ITEM = 1;
    }

    public interface IItemClickListener<TItem> {
        void click(int position, TItem item);
    }

   /* private var mTryAgainClickListener: (() -> Unit)? = null
    private int mEmptyMessageResId = R.string.the_list_is_empty;

    protected CustomRecyclerViewAdapter() : this(ArrayList<TItem>())

    protected CustomRecyclerViewAdapter(tryAgainClickListener: (() -> Unit)?) : this() {
        mTryAgainClickListener = tryAgainClickListener
    }

    protected CustomRecyclerViewAdapter(@StringRes int emptyMessageResId, tryAgainClickListener: (() -> Unit)?) : this() {
        mEmptyMessageResId = emptyMessageResId
        mTryAgainClickListener = tryAgainClickListener
    }

    @interface ViewType {
        int GRID_STATUS = 0;
        int ITEM = 1;
    }

    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.GRID_STATUS) {
            View itemView = LayoutInflater.from(parent.context).inflate(R.layout.grid_status, parent, false)
            return GridStatusViewHolder(itemView, mTryAgainClickListener, mEmptyMessageResId)
        }
        return onCreateItemViewHolder(parent, viewType)
    }

    override void onBindViewHolder(holder: CustomRecyclerViewHolder, position: Int) {
        if (holder.itemViewType == ViewType.GRID_STATUS) {
            val gridStatusViewHolder = holder as GridStatusViewHolder
            gridStatusViewHolder.bind(mRequestStatus, mItems.size)
            return
        }


        onBindItemViewHolder(holder as THolder, position)
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.invoke(holder.adapterPosition, mItems[holder.adapterPosition])
        }
    }

    override void getItemCount(): Int {
        return mItems.size + if (mRequestStatus == RequestStatusDescriptor.HIDDEN) 0 else 1 // List status.
    }

    final override void getItemViewType(position: Int): Int {
        if (position == mItems.size) {
            return ViewType.GRID_STATUS
        }
        val itemViewType = getItemViewTypeOverride(position)
        if (itemViewType == ViewType.GRID_STATUS) {
            throw InvalidParameterException("The view type must be different of ${ViewType.GRID_STATUS}")
        }
        return itemViewType
    }

    open protected void getItemViewTypeOverride(position: Int): Int {
        return ViewType.ITEM
    }

    protected void getItemByPosition(position: Int): TItem {
        return mItems[position]
    }

    void addItems(items:List<TItem>) {
        hideRequestStatus()

        val itemCount = mItems.size
        mItems.addAll(items)
        notifyItemRangeInserted(itemCount, items.size)
    }

    void replaceItems(items: List<TItem>) {
        redrawGridStatus(RequestStatusDescriptor.HIDDEN)

        clearItems()

        mItems.addAll(items)
        notifyItemRangeInserted(0, items.size)
    }

    void clearItems() {
        val itemCount = mItems.size
        if (itemCount > 0) {
            mItems.clear()
            notifyItemRangeRemoved(0, itemCount)
        }
    }

    void removeItemByIndex(index: Int) {
        mItems.removeAt(index)
        notifyItemRemoved(index)
    }

    void insertItemByIndex(item: TItem, index: Int) {
        mItems.add(index, item)
        notifyItemInserted(index)
    }

    val items: List<TItem>
    get() = mItems

    void showLoading() {
        redrawGridStatus(RequestStatusDescriptor.LOADING)
    }

    void hideRequestStatus() {
        redrawGridStatus(RequestStatusDescriptor.HIDDEN)
    }

    void hideLoadingIndicator() {
        if (mRequestStatus == RequestStatusDescriptor.LOADING) { // Hide only if is loading
            hideRequestStatus()
        }
    }

    void showEmptyMessage() {
        redrawGridStatus(RequestStatusDescriptor.EMPTY)
    }

    void showErrorMessage() {
        redrawGridStatus(RequestStatusDescriptor.ERROR)
    }

    private void redrawGridStatus(gridStatus: Int) {
        Timber.i("REDRAWING THE GRID STATUS: " + gridStatus)
        val previousRequestStatus = mRequestStatus
        mRequestStatus = gridStatus
        if (mRequestStatus == RequestStatusDescriptor.HIDDEN) {
            notifyItemRemoved(mItems.size)
        } else if (previousRequestStatus == RequestStatusDescriptor.HIDDEN) {
            notifyItemInserted(mItems.size)
        } else {
            notifyItemChanged(mItems.size)
        }
    }

    void setOnItemClickListener(onItemClickListener: (position: Int, item: TItem) -> Unit) {
        this.mOnItemClickListener = onItemClickListener
    }

    protected abstract void onCreateItemViewHolder(parent: ViewGroup, viewType: Int): THolder

    protected abstract void onBindItemViewHolder(holder: THolder, position: Int)*/
}
