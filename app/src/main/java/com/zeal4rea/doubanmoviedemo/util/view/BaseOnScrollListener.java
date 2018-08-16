package com.zeal4rea.doubanmoviedemo.util.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class BaseOnScrollListener extends RecyclerView.OnScrollListener {
    private boolean loading = false;
    private int loadThreshold = 5;
    private int currentPage = 0;
    private long token;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (!isLoading() && canLoadMore(recyclerView)) {
            onLoad(token = System.currentTimeMillis());
            setLoading(true);
        }
    }

    private void reset() {
        loading = false;
        loadThreshold = 5;
        currentPage = 5;
    }

    public void refresh() {
        setLoading(true);
        reset();
        onLoad(token = System.currentTimeMillis());
    }

    private void onLoad(long token) {
    }

    private boolean canLoadMore(RecyclerView recyclerView) {
        RecyclerView.LayoutManager mLayoutManager = getLayoutManager();
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int firstVisibleItem = 0;
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            firstVisibleItem = ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(null)[0];
        } else if (mLayoutManager instanceof GridLayoutManager) {
            firstVisibleItem = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            firstVisibleItem = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        }

        return (totalItemCount - visibleItemCount) <= (firstVisibleItem + this.loadThreshold);
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public abstract RecyclerView.LayoutManager getLayoutManager();

    public abstract RecyclerView.Adapter getAdapter();

    public abstract SwipeRefreshLayout getRefreshLayout();
}
