package com.zeal4rea.doubanmoviedemo.data.local;

import com.zeal4rea.doubanmoviedemo.bean.api.Celebrity;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Comment4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.PhotoTemp;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Subject4J;
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
    public Observable<CommonResult<Void, Subject>> apiGetSubjects(SubjectsType type) {
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
    public Observable<CommonResult<Void, PhotoTemp>> htmlGetPhotos(String subjectId, int start) {
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
}
