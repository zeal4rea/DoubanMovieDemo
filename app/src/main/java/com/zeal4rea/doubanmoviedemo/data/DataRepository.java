package com.zeal4rea.doubanmoviedemo.data;

import com.zeal4rea.doubanmoviedemo.bean.api.Celebrity;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Comment4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.PhotoTemp;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Subject4J;
import com.zeal4rea.doubanmoviedemo.data.local.LocalDataSource;
import com.zeal4rea.doubanmoviedemo.data.remote.RemoteDataSource;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class DataRepository implements DataSource {
    private static DataRepository INSTANCE = null;
    private final LocalDataSource localDataSource;
    private final RemoteDataSource remoteDataSource;

    private DataRepository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public static DataRepository getInstance(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<CommonResult<Void, Subject>> apiGetSubjects(SubjectsType type) {
        return remoteDataSource.apiGetSubjects(type);
    }

    @Override
    public Observable<CommonResult<Void, Subject>> apiQuerySubjects(Map<String, String> queryMap) {
        return null;
    }

    @Override
    public Observable<Subject> apiGetSubjectDetail(String subjectId) {
        return remoteDataSource.apiGetSubjectDetail(subjectId);
    }

    @Override
    public Observable<Celebrity> apiGetCelebrity(String celebrityId) {
        return remoteDataSource.apiGetCelebrity(celebrityId);
    }

    @Override
    public Observable<Subject4J> htmlGetSubjectDetail(String subjectId) {
        return remoteDataSource.htmlGetSubjectDetail(subjectId);
    }

    @Override
    public Observable<CommonResult<Void, PhotoTemp>> htmlGetPhotos(String subjectId, int start) {
        return remoteDataSource.htmlGetPhotos(subjectId, start);
    }

    @Override
    public Observable<List<Comment4J>> htmlGetComments(String subjectId, int start, String sort) {
        return remoteDataSource.htmlGetComments(subjectId, start, sort);
    }

    @Override
    public Observable<List<Review4J>> htmlGetReviews(String subjectId, int start, int count) {
        return remoteDataSource.htmlGetReviews(subjectId, start, count);
    }
}