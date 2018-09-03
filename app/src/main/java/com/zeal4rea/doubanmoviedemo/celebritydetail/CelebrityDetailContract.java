package com.zeal4rea.doubanmoviedemo.celebritydetail;

import com.zeal4rea.doubanmoviedemo.base.BasePresenter;
import com.zeal4rea.doubanmoviedemo.base.BaseView;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Celebrity;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.CelebrityWork;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Photo;

import java.util.List;

public interface CelebrityDetailContract {
    interface View extends BaseView<Presenter> {
        void setUpTitle(String title);
        void displayBasicInfo(Celebrity celebrity);
        void displayPhotos(List<Photo> photos, boolean hasMore);
        void displayWorks(List<CelebrityWork> works);
        void displayRelatedCelebrities(List<Celebrity.RelatedCelebrity> relatedCelebrities);
        void displayNoPhoto();
        void displayNoWork();
        void displayNoRelatedCelebrity();
        void displayErrorPage();
        void loading(boolean loading);
    }

    interface Presenter extends BasePresenter {
        void load();
    }
}
