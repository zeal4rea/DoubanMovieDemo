package com.zeal4rea.doubanmoviedemo.data.remote.jsoup;

import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Celebrity4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Comment4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.PhotoTemp;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Subject4J;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsoupApi {
    String SORT_NEW_SCORE = "new_score";
    String SORT_TIME = "time";
    String[] PHOTO_TYPE = {"S", "R", "W"};

    @GET("{url}")
    Observable<String> getHtml(@Path("url") String url);

    @GET("/movie/subject/{subjectId}/")
    Observable<Subject4J> getSubjectDetail(@Path("subjectId") String subjectId);

    @GET("/j/fetch_photo/")
    Observable<CommonResult<Void, PhotoTemp>> getPhotos(@Query("mid") String subjectId, @Query("start") int start, @Query("type") String type);

    @GET("/movie/subject/{subjectId}/comments")
    Observable<List<Comment4J>> getComments(@Path("subjectId") String subjectId, @Query("start") int start, @Query("sort") String sort);

    @GET("/movie/subject/{subjectId}/reviews?from=subject")
    Observable<List<Review4J>> getReviews(@Path("subjectId") String subjectId, @Query("start") int start, @Query("count") int count);

    @GET("/movie/celebrity/{id}/")
    Observable<Celebrity4J> getCelebrityDetail(@Path("id") String id);
}
