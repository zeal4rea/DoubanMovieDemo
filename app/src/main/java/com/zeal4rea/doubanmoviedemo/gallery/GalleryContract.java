package com.zeal4rea.doubanmoviedemo.gallery;

import com.zeal4rea.doubanmoviedemo.base.BasePresenter;
import com.zeal4rea.doubanmoviedemo.base.BaseView;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Photo4J;

import java.util.List;

public interface GalleryContract {
    interface View extends BaseView<Presenter> {
        void load(List<Photo4J> photos, boolean add, boolean hasMore);
        void displayEmptyPage();
        void displayErrorPage();
        void loading(boolean loading);
    }

    interface Presenter extends BasePresenter {
        void load(boolean more);
    }
}
