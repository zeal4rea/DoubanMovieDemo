package com.zeal4rea.doubanmoviedemo.subjectdetail;

import com.zeal4rea.doubanmoviedemo.base.BasePresenter;
import com.zeal4rea.doubanmoviedemo.base.BaseView;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Photo4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Celebrity;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Comment;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Subject;

import java.util.List;

public interface SubjectDetailContract {
    interface View extends BaseView<Presenter> {
        void displayBasicInfo(Subject subject);
        void displayCelebrities(List<Celebrity> celebrities);
        void displayPhotos(List<Photo4J> photos);
        void displayComments(List<Comment> comments);
        void displayReviews(List<Review4J> reviews);
        void displayNoCelebrities();
        void displayNoPhotos();
        void displayNoComments();
        void displayNoReviews();
        void displayErrorPage();
        void loading(boolean loading);
    }
    interface Presenter extends BasePresenter {
        void load();
    }
}
