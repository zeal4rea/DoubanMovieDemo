package com.zeal4rea.doubanmoviedemo.gallerytabs;

import android.util.SparseArray;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseApplication;
import com.zeal4rea.doubanmoviedemo.base.BaseContants;
import com.zeal4rea.doubanmoviedemo.util.Utils;

public class GalleryTabsPresenter implements GalleryTabsContract.Presenter {
    private GalleryTabsContract.View mTabsView;
    private int mType;

    GalleryTabsPresenter(GalleryTabsContract.View tabsView, int type) {
        mTabsView = tabsView;
        mType = type;
        mTabsView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        try {
            int id = 0;
            switch (mType) {
                case BaseContants.TYPE_GALLERY_SUBJECT:
                    id = R.array.subject_photo_list_order;
                    break;
                case BaseContants.TYPE_GALLERY_CELEBRITY:
                    id = R.array.celebrity_photo_list_order;
                    break;
            }
            SparseArray<String> titles = Utils.getSparseArraySplitBy(BaseApplication.getContext().getResources().getStringArray(id), "=");;
            mTabsView.addFragments(titles, mType);
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
