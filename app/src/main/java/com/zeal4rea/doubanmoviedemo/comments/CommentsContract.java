package com.zeal4rea.doubanmoviedemo.comments;

import com.zeal4rea.doubanmoviedemo.base.BasePresenter;
import com.zeal4rea.doubanmoviedemo.base.BaseView;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Comment;

import java.util.List;

public interface CommentsContract {
    interface View extends BaseView<Presenter> {
        void setUpTitle(String title);
        void loading(boolean loading);
        void fillData(List<Comment> comments, boolean add, boolean hasMore);
        void emptyRecyclerView();
        void displayErrorPage();
        void displayEmptyPage();
    }
    interface Presenter extends BasePresenter {
        void load(boolean more);
    }
}
