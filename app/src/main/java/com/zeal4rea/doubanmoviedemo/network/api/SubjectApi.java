package com.zeal4rea.doubanmoviedemo.network.api;

import com.zeal4rea.doubanmoviedemo.bean.Comment;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.Photo;
import com.zeal4rea.doubanmoviedemo.bean.Review;
import com.zeal4rea.doubanmoviedemo.bean.Subject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SubjectApi {
    @GET("/v2/movie/subject/{id}")
    Observable<Subject> getSubject(@Path("id") int id);
    @GET("/v2/movie/subject/{id}/photos")
    Observable<CommonResult<Subject, Photo>> getSubjectPhotos(@Path("id") int id);
    @GET("/v2/moview/subject/{id}/reviews")
    Observable<CommonResult<Subject, Review>> getSubjectReviews(@Path("id") int id);
    @GET("/v2/moview/subject/{id}/comments")
    Observable<CommonResult<Subject, Comment>> getSubjectComments(@Path("id") int id);
}
