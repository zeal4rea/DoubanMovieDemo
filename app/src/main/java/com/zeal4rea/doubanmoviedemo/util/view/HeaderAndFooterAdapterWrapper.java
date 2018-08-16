package com.zeal4rea.doubanmoviedemo.util.view;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class HeaderAndFooterAdapterWrapper extends RecyclerView.Adapter {
    private static final int BASE_ITEM_TYPE_HEADER = 10000;
    private static final int BASE_ITEM_TYPE_FOOTER = 20000;

    private SparseArrayCompat<View> mHeaders = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooters = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;

    public HeaderAndFooterAdapterWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaders.get(viewType) != null) {
            return HeaderAndFooterViewHolder.createViewHolder(mHeaders.get(viewType));
        }
        if (mFooters.get(viewType) != null) {
            return HeaderAndFooterViewHolder.createViewHolder(mFooters.get(viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return mHeaders.keyAt(position);
        }
        if (isFooterPosition(position)) {
            return mFooters.keyAt(position - getRealItemCount() - getHeadersCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position)) {
            return;
        }
        if (isFooterPosition(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    public void addHeaderView(View view) {
        mHeaders.put(getHeadersCount() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFooterView(View view) {
        mFooters.put(getFootersCount() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public int getHeadersCount() {
        return mHeaders.size();
    }

    public int getFootersCount() {
        return mFooters.size();
    }

    private boolean isHeaderPosition(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterPosition(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }
}
