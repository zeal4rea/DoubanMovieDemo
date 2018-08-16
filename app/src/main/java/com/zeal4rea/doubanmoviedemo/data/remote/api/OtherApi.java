package com.zeal4rea.doubanmoviedemo.data.remote.api;

import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OtherApi {
    @GET("/v2/movie/top250")
    Observable<CommonResult<Void, Subject>> getTop250();
    @GET("/v2/movie/us_box")
    Observable<CommonResult<Void, Subject>> getUsBox();
    @GET("/v2/movie/weekly")
    Observable<CommonResult<Void, Subject>> getWeekly();
    @GET("/v2/movie/new_movies")
    Observable<CommonResult<Void, Subject>> getNewMovies();
    @GET("/v2/movie/in_theaters")
    Observable<CommonResult<Void, Subject>> getInTheaters(@Nullable @Query("city") String city);
    @GET("/v2/movie/coming_soon")
    Observable<CommonResult<Void, Subject>> getComingSoon(@Nullable @Query("start") int start, @Nullable @Query("count") int count);
}
