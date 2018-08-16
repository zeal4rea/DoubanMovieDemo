package com.zeal4rea.doubanmoviedemo.data.remote.api;

import com.zeal4rea.doubanmoviedemo.bean.api.Comment;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Photo;
import com.zeal4rea.doubanmoviedemo.bean.api.Review;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SubjectApi {
    @GET("/v2/movie/subject/{id}")
    Observable<Subject> getSubjectDetail(@Path("id") String id);
    @GET("/v2/movie/subject/{id}/photos")
    Observable<CommonResult<Subject, Photo>> getSubjectPhotos(@Path("id") String id);
    @GET("/v2/movie/subject/{id}/reviews")
    Observable<CommonResult<Subject, Review>> getSubjectReviews(@Path("id") String id);
    @GET("/v2/movie/subject/{id}/comments")
    Observable<CommonResult<Subject, Comment>> getSubjectComments(@Path("id") String id);
}
