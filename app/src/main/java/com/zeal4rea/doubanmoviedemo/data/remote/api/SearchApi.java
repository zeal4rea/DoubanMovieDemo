package com.zeal4rea.doubanmoviedemo.data.remote.api;

import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface SearchApi {
    @GET("/v2/movie/search")
    Observable<CommonResult<Void, Subject>> getQueryResult(@QueryMap Map<String, String> queryMap);
}
