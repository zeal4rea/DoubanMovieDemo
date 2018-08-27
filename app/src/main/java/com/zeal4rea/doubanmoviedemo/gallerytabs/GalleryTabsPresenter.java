package com.zeal4rea.doubanmoviedemo.gallerytabs;

import android.util.SparseArray;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseApplication;
import com.zeal4rea.doubanmoviedemo.util.Utils;

public class GalleryTabsPresenter implements GalleryTabsContract.Presenter {
    private GalleryTabsContract.View mTabsView;

    GalleryTabsPresenter(GalleryTabsContract.View tabsView) {
        mTabsView = tabsView;
        mTabsView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        try {
            SparseArray<String> titles = Utils.getSparseArraySplitBy(BaseApplication.getContext().getResources().getStringArray(R.array.photo_list_order), "=");
            mTabsView.addFragments(titles);
            mTabsView.initComplete();
        } catch (Exception e) {
            e.printStackTrace();
            mTabsView.displayErrorPage();
        }
    }

    @Override
    public void unSubscribe() {
    }
}
