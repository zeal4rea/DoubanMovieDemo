package com.zeal4rea.doubanmoviedemo.data.remote.api;

import com.zeal4rea.doubanmoviedemo.bean.api.Celebrity;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Photo;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CelebrityApi {
    @GET("/v2/movie/celebrity/{id}")
    Observable<Celebrity> getCelebrity(@Path("id") String id);
    @GET("/v2/movie/celebrity/{id}/works")
    Observable<CommonResult<Celebrity, Subject>> getCelebrityWorks(@Path("id") String id);
    @GET("/v2/movie/celebrity/{id}/photos")
    Observable<CommonResult<Celebrity, Photo>> getCelebrityPhotos(@Path("id") String id);
}
