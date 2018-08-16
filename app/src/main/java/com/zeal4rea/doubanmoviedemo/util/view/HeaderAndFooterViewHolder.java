package com.zeal4rea.doubanmoviedemo.util.view;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class HeaderAndFooterViewHolder extends RecyclerView.ViewHolder {
    private SparseArrayCompat<View> mViews;
    private View mItemView;

    public HeaderAndFooterViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mViews = new SparseArrayCompat<>();
    }

    public static HeaderAndFooterViewHolder createViewHolder(View itemView) {
        return new HeaderAndFooterViewHolder(itemView);
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getItemView() {
        return mItemView;
    }
}
