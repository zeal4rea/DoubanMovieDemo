package com.zeal4rea.doubanmoviedemo.query;

import com.zeal4rea.doubanmoviedemo.base.BasePresenter;
import com.zeal4rea.doubanmoviedemo.base.BaseView;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;

import java.util.List;

public interface QueryContract {
    interface View extends BaseView<Presenter> {
        void load(List<Subject> subjects, boolean add, boolean hasMore);
        void displayEmptyPage();
        void displayErrorPage();
        void loading(boolean loading);
        void setTitle(String title);
    }

    interface Presenter extends BasePresenter {
        void query(boolean more);
    }
}
