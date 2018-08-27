package com.zeal4rea.doubanmoviedemo.data.remote.rexxar;

import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Celebrity;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.CelebrityWorkWrapper;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Comment;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.SubjectCelebrities;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Photo;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Subject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RexxarApi {
    String SORT_WORK_TIME = "time";
    String SORT_WORK_VOTE = "vote";
    String SORT_COMMENT_HOT = "hot";
    String SORT_COMMENT_LATEST = "latest";
    String refererCelebrity = "https://m.douban.com/movie/celebrity/%s/";
    String refererCelebrityAllPhotos = "https://m.douban.com/movie/celebrity/%s/all_photos";
    String refererSubject = "https://m.douban.com/movie/subject/%s/";
    String refererSubjectComments = "https://m.douban.com/movie/subject/%s/comments?from=subject";

    @GET("/rexxar/api/v2/elessar/subject/{id}")
    Observable<Subject> getSubjectInfo(@Header("Referer") String referer, @Path("id") String subjectId);

    @GET("/rexxar/api/v2/movie/{id}/credits")
    Observable<SubjectCelebrities> getSubjectCelebrities(@Header("Referer") String referer, @Path("id") String subjectId);

    @GET("/rexxar/api/v2/movie/{id}/interests?ck=no_8&for_mobile=1")
    Observable<CommonResult<Void, Comment>> getSubjectComments(@Header("Referer") String referer, @Path("id") String subjectId, @Query("sort") String sort, @Query("start") int start, @Query("count") int count);

    @GET("/rexxar/api/v2/celebrity/{id}/")
    Observable<Celebrity> getCelebrityDetail(@Header("Referer") String referer, @Path("id") String id);

    @GET("/rexxar/api/v2/celebrity/{id}/works")
    Observable<CommonResult<Void, CelebrityWorkWrapper>> getCelebrityWorks(@Header("Referer") String referer, @Path("id") String id, @Query("sort") String sort, @Query("start") int start, @Query("count") int count);

    @GET("/rexxar/api/v2/celebrity/{id}/photos")
    Observable<CommonResult<Void, Photo>> getCelebrityPhoto(@Header("Referer") String referer, @Path("id") String id, @Query("start") int start, @Query("count") int count);
}
