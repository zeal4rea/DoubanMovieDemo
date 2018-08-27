package com.zeal4rea.doubanmoviedemo.reviews;

import com.zeal4rea.doubanmoviedemo.base.BasePresenter;
import com.zeal4rea.doubanmoviedemo.base.BaseView;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;

import java.util.List;

public interface ReviewsContract {
    interface View extends BaseView<Presenter> {
        void fillData(List<Review4J> reviews, boolean add, boolean hasMore);
        void displayEmptyPage();
        void displayErrorPage();
        void emptyRecyclerView();
        void loading(boolean loading);
    }

    interface Presenter extends BasePresenter {
        void load(boolean more);
    }
}
