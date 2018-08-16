package com.zeal4rea.doubanmoviedemo.subjectdetail;

import com.zeal4rea.doubanmoviedemo.base.BasePresenter;
import com.zeal4rea.doubanmoviedemo.base.BaseView;

public interface SubjectDetailContract {
    interface View extends BaseView<Presenter> {
        void displayDetail();
        void displayErrorPage();
    }
    interface Presenter extends BasePresenter {
    }
}
