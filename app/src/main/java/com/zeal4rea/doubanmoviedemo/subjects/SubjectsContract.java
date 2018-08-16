package com.zeal4rea.doubanmoviedemo.subjects;

import com.zeal4rea.doubanmoviedemo.base.BasePresenter;
import com.zeal4rea.doubanmoviedemo.base.BaseView;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;

public interface SubjectsContract {
    interface View extends BaseView<Presenter> {
        void displaySubjects(CommonResult<Void, Subject> result, boolean refresh);
        void displayErrorPage();
        void displayEmptyPage();
        void openSubjectDetail(Subject subject);
        void loading(boolean loading);
    }
    interface Presenter extends BasePresenter {
        void openSubjectDetail(Subject subject);
        void refreshSubjects();
        void loadMore();
    }
}
