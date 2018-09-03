package com.zeal4rea.doubanmoviedemo.gallerytabs;

import android.util.SparseArray;

import com.zeal4rea.doubanmoviedemo.base.BasePresenter;
import com.zeal4rea.doubanmoviedemo.base.BaseView;

public interface GalleryTabsContract {
    interface View extends BaseView<Presenter> {
        void addFragments(SparseArray<String> titles, int type);
        void displayErrorPage();
        void initComplete();
    }

    interface Presenter extends BasePresenter {
    }
}
