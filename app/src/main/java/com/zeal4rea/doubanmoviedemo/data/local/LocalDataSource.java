package com.zeal4rea.doubanmoviedemo.data.local;

import com.zeal4rea.doubanmoviedemo.bean.api.Celebrity;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Comment4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.PhotoTemp;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Subject4J;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.CelebrityWorkWrapper;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Comment;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Photo;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.SubjectCelebrities;
import com.zeal4rea.doubanmoviedemo.data.DataSource;
import com.zeal4rea.doubanmoviedemo.data.SubjectsType;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class LocalDataSource implements DataSource {
    private static LocalDataSource INSTANCE = null;

    private LocalDataSource() {
    }

    public static LocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<CommonResult<Void, Subject>> apiGetSubjects(SubjectsType type, int start, int count) {
        return null;
    }

    @Override
    public Observable<CommonResult<Void, Subject>> apiQuerySubjects(Map<String, String> queryMap) {
        return null;
    }

    @Override
    public Observable<Subject> apiGetSubjectDetail(String subjectId) {
        return null;
    }

    @Override
    public Observable<Celebrity> apiGetCelebrity(String celebrityId) {
        return null;
    }

    @Override
    public Observable<Subject4J> htmlGetSubjectDetail(String subjectId) {
        return null;
    }

    @Override
    public Observable<CommonResult<Void, PhotoTemp>> htmlGetPhotos(String subjectId, int start, int type) {
        return null;
    }

    @Override
    public Observable<List<Comment4J>> htmlGetComments(String subjectId, int start, String sort) {
        return null;
    }

    @Override
    public Observable<List<Review4J>> htmlGetReviews(String subjectId, int start, int count) {
        return null;
    }

    @Override
    public Observable<com.zeal4rea.doubanmoviedemo.bean.rexxar.Subject> rexxarGetSubjectInfo(String id) {
        return null;
    }

    @Override
    public Observable<SubjectCelebrities> rexxarGetSubjectCelebrities(String id) {
        return null;
    }

    @Override
    public Observable<CommonResult<Void, Comment>> rexxarGetSubjectComments(String id, String sort, int start, int count) {
        return null;
    }

    @Override
    public Observable<com.zeal4rea.doubanmoviedemo.bean.rexxar.Celebrity> rexxarGetCelebrityDetail(String id) {
        return null;
    }

    @Override
    public Observable<CommonResult<Void, CelebrityWorkWrapper>> rexxarGetCelebrityWorks(String id, String sort, int start, int count) {
        return null;
    }

    @Override
    public Observable<CommonResult<Void, Photo>> rexxarGetCelebrityPhotos(String id, int start, int count) {
        return null;
    }
}
