package com.zeal4rea.doubanmoviedemo.celebrityworks;

import com.zeal4rea.doubanmoviedemo.base.BasePresenter;
import com.zeal4rea.doubanmoviedemo.base.BaseView;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.CelebrityWorkWrapper;

import java.util.List;

public interface CelebrityWorksContract {
    interface View extends BaseView<Presenter> {
        void load(List<CelebrityWorkWrapper> works, boolean add, boolean hasMore);
        void displayEmptyPage();
        void displayErrorPage();
        void loading(boolean loading);
    }

    interface Presenter extends BasePresenter {
        void load(boolean more);
    }
}
