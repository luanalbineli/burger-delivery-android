package com.burgerdelivery.ui.recyclerview;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.burgerdelivery.R;
import com.burgerdelivery.enunn.RequestStatus;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class CustomRecyclerViewAdapter<TItem, THolder extends CustomRecyclerViewHolder> extends RecyclerView.Adapter<CustomRecyclerViewHolder> {
    private final List<TItem> mItems;
    //private var mOnItemClickListener: ((position: Int, item: TItem) -> Unit)? = null
    private @RequestStatus int mRequestStatus = RequestStatus.HIDDEN;

    private int mEmptyMessageResId = R.string.the_list_is_empty;

    private @Nullable ITryAgainListener mTryAgainClickListener;

    protected CustomRecyclerViewAdapter() {
        this(new ArrayList<TItem>());
    }

    private CustomRecyclerViewAdapter(List<TItem> items) {
        this.mItems = items;
    }

    protected CustomRecyclerViewAdapter(@StringRes int emptyMessageResId, ITryAgainListener tryAgainClickListener) {
        this();

        mEmptyMessageResId = emptyMessageResId;
        mTryAgainClickListener = tryAgainClickListener;
    }

    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.GRID_STATUS) {
            View itemView = LayoutInflater.from(parent.context).inflate(R.layout.grid_status, parent, false)
            return GridStatusViewHolder(itemView, mTryAgainClickListener, mEmptyMessageResId)
        }
        return onCreateItemViewHolder(parent, viewType)
    }

    public interface ITryAgainListener {
        void tryAgain();
    }

    @interface ViewType {
        int GRID_STATUS = 0;
        int ITEM = 1;
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

    override fun onBindViewHolder(holder: CustomRecyclerViewHolder, position: Int) {
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

    override fun getItemCount(): Int {
        return mItems.size + if (mRequestStatus == RequestStatusDescriptor.HIDDEN) 0 else 1 // List status.
    }

    final override fun getItemViewType(position: Int): Int {
        if (position == mItems.size) {
            return ViewType.GRID_STATUS
        }
        val itemViewType = getItemViewTypeOverride(position)
        if (itemViewType == ViewType.GRID_STATUS) {
            throw InvalidParameterException("The view type must be different of ${ViewType.GRID_STATUS}")
        }
        return itemViewType
    }

    open protected fun getItemViewTypeOverride(position: Int): Int {
        return ViewType.ITEM
    }

    protected fun getItemByPosition(position: Int): TItem {
        return mItems[position]
    }

    fun addItems(items:List<TItem>) {
        hideRequestStatus()

        val itemCount = mItems.size
        mItems.addAll(items)
        notifyItemRangeInserted(itemCount, items.size)
    }

    fun replaceItems(items: List<TItem>) {
        redrawGridStatus(RequestStatusDescriptor.HIDDEN)

        clearItems()

        mItems.addAll(items)
        notifyItemRangeInserted(0, items.size)
    }

    fun clearItems() {
        val itemCount = mItems.size
        if (itemCount > 0) {
            mItems.clear()
            notifyItemRangeRemoved(0, itemCount)
        }
    }

    fun removeItemByIndex(index: Int) {
        mItems.removeAt(index)
        notifyItemRemoved(index)
    }

    fun insertItemByIndex(item: TItem, index: Int) {
        mItems.add(index, item)
        notifyItemInserted(index)
    }

    val items: List<TItem>
    get() = mItems

    fun showLoading() {
        redrawGridStatus(RequestStatusDescriptor.LOADING)
    }

    fun hideRequestStatus() {
        redrawGridStatus(RequestStatusDescriptor.HIDDEN)
    }

    fun hideLoadingIndicator() {
        if (mRequestStatus == RequestStatusDescriptor.LOADING) { // Hide only if is loading
            hideRequestStatus()
        }
    }

    fun showEmptyMessage() {
        redrawGridStatus(RequestStatusDescriptor.EMPTY)
    }

    fun showErrorMessage() {
        redrawGridStatus(RequestStatusDescriptor.ERROR)
    }

    private fun redrawGridStatus(gridStatus: Int) {
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

    fun setOnItemClickListener(onItemClickListener: (position: Int, item: TItem) -> Unit) {
        this.mOnItemClickListener = onItemClickListener
    }

    protected abstract fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): THolder

    protected abstract fun onBindItemViewHolder(holder: THolder, position: Int)*/
}
