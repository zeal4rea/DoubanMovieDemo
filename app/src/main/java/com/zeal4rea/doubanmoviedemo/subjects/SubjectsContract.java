package com.zeal4rea.doubanmoviedemo.subjects;

import com.zeal4rea.doubanmoviedemo.base.BasePresenter;
import com.zeal4rea.doubanmoviedemo.base.BaseView;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;

import java.util.List;

public interface SubjectsContract {
    interface View extends BaseView<Presenter> {
        void load(List<Subject> subjects, boolean add, boolean hasMore);
        void displayErrorPage();
        void displayEmptyPage();
        void emptyRecyclerView();
        void loading(boolean loading);
    }
    interface Presenter extends BasePresenter {
        void load(boolean more);
    }
}
