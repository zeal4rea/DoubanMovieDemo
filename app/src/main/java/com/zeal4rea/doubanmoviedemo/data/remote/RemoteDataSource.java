package com.zeal4rea.doubanmoviedemo.data.remote;

import android.util.Log;

import com.zeal4rea.doubanmoviedemo.BuildConfig;
import com.zeal4rea.doubanmoviedemo.base.BaseApplication;
import com.zeal4rea.doubanmoviedemo.base.BaseContants;
import com.zeal4rea.doubanmoviedemo.bean.api.Celebrity;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Comment4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.PhotoTemp;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Subject4J;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.SubjectCelebrities;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.CelebrityWorkWrapper;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Comment;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Photo;
import com.zeal4rea.doubanmoviedemo.data.DataSource;
import com.zeal4rea.doubanmoviedemo.data.SubjectsType;
import com.zeal4rea.doubanmoviedemo.data.remote.api.CelebrityApi;
import com.zeal4rea.doubanmoviedemo.data.remote.api.OtherApi;
import com.zeal4rea.doubanmoviedemo.data.remote.api.SubjectApi;
import com.zeal4rea.doubanmoviedemo.data.remote.jsoup.JsoupApi;
import com.zeal4rea.doubanmoviedemo.data.remote.rexxar.RexxarApi;
import com.zeal4rea.doubanmoviedemo.util.network.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSource implements DataSource {
    private static RemoteDataSource INSTANCE = null;
    private static OkHttpClient okHttpClient;
    private static Converter.Factory customConverterFactory = CustomConverterFactory.create();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static Retrofit.Builder retrofitBuilder;

    static {
        File cacheFile = new File(BaseApplication.getInstance().getCacheDir(), "networkCache");
        Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);
        Interceptor netInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (NetworkUtil.isNetworkAvaliable()) {
                    //有网络缓存60s
                    Response response = chain.proceed(request);
                    int maxAge = 60;
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("cache-control", "public, max-age=" + maxAge)
                            .build();
                }
                return chain.proceed(request);
            }
        };
        Interceptor noNetInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //Request request = original.newBuilder().header("User-Agent", BaseContants.USER_AGENT).build();
                if (!NetworkUtil.isNetworkAvaliable()) {
                    //无网络强制从缓存取，过期时间365天
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                    Response response = chain.proceed(request);
                    int maxStale = 365 * 24 * 60 * 60;
                    return response.newBuilder()
                            .removeHeader("pragma")
                            .header("cache-control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
                return chain.proceed(request);
            }
        };
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(noNetInterceptor)
                .addNetworkInterceptor(netInterceptor);
        if (BuildConfig.showLog) {
            httpClientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d(BaseContants.NETWORK_TAG, message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        okHttpClient = httpClientBuilder.build();
        retrofitBuilder = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BaseContants.BASE_URL_API)
                .addConverterFactory(customConverterFactory)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory);
    }

    private RemoteDataSource() {
    }

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    private Object getApi(String baseUrl, Class service) {
        return retrofitBuilder.baseUrl(baseUrl).build().create(service);
    }

    @Override
    public Observable<CommonResult<Void, Subject>> apiGetSubjects(SubjectsType type, int start, int count) {
        String baseUrl = BaseContants.BASE_URL_API;
        switch (type) {
            case IN_THEATERS:
                return ((OtherApi) getApi(baseUrl, OtherApi.class)).getInTheaters(null, start, count);
            case COMING_SOON:
                return ((OtherApi) getApi(baseUrl, OtherApi.class)).getComingSoon(start, count);
            case TOP250:
                return ((OtherApi) getApi(baseUrl, OtherApi.class)).getTop250();
            /*case US_BOX:
                break;
            case WEEKLY:
                break;*/
        }
        return null;
    }

    @Override
    public Observable<Subject> apiGetSubjectDetail(String subjectId) {
        return ((SubjectApi) getApi(BaseContants.BASE_URL_API, SubjectApi.class)).getSubjectDetail(subjectId);
    }

    @Override
    public Observable<Celebrity> apiGetCelebrity(String celebrityId) {
        return ((CelebrityApi) getApi(BaseContants.BASE_URL_API, CelebrityApi.class)).getCelebrity(celebrityId);
    }

    @Override
    public Observable<Subject4J> htmlGetSubjectDetail(String subjectId) {
        return ((JsoupApi) getApi(BaseContants.BASE_URL_MOBILE, JsoupApi.class)).getSubjectDetail(subjectId);
    }

    @Override
    public Observable<CommonResult<Void, PhotoTemp>> htmlGetPhotos(String subjectId, int start, int type) {
        return ((JsoupApi) getApi(BaseContants.BASE_URL_MOBILE, JsoupApi.class)).getPhotos(subjectId, start, JsoupApi.PHOTO_TYPE[type]);
    }

    @Override
    public Observable<List<Comment4J>> htmlGetComments(String subjectId, int start, String sort) {
        return ((JsoupApi) getApi(BaseContants.BASE_URL, JsoupApi.class)).getComments(subjectId, start, sort);
    }

    @Override
    public Observable<List<Review4J>> htmlGetReviews(String subjectId, int start, int count) {
        return ((JsoupApi) getApi(BaseContants.BASE_URL_MOBILE, JsoupApi.class)).getReviews(subjectId, start, count);
    }

    @Override
    public Observable<CommonResult<Void, Subject>> apiQuerySubjects(Map<String, String> queryMap) {
        return null;
    }

    @Override
    public Observable<com.zeal4rea.doubanmoviedemo.bean.rexxar.Subject> rexxarGetSubjectInfo(String id) {
        String referer = String.format(RexxarApi.refererSubject, id);
        return ((RexxarApi) getApi(BaseContants.BASE_URL_MOBILE, RexxarApi.class)).getSubjectInfo(referer, id);
    }

    @Override
    public Observable<SubjectCelebrities> rexxarGetSubjectCelebrities(String id) {
        String referer = String.format(RexxarApi.refererSubject, id);
        return ((RexxarApi) getApi(BaseContants.BASE_URL_MOBILE, RexxarApi.class)).getSubjectCelebrities(referer, id);
    }

    @Override
    public Observable<CommonResult<Void, Comment>> rexxarGetSubjectComments(String id, String sort, int start, int count) {
        String referer = String.format(RexxarApi.refererSubjectComments, id);
        return ((RexxarApi) getApi(BaseContants.BASE_URL_MOBILE, RexxarApi.class)).getSubjectComments(referer, id, sort, start, count);
    }

    @Override
    public Observable<com.zeal4rea.doubanmoviedemo.bean.rexxar.Celebrity> rexxarGetCelebrityDetail(String id) {
        String referer = String.format(RexxarApi.refererCelebrity, id);
        return ((RexxarApi) getApi(BaseContants.BASE_URL_MOBILE, RexxarApi.class)).getCelebrityDetail(referer, id);
    }

    @Override
    public Observable<CommonResult<Void, CelebrityWorkWrapper>> rexxarGetCelebrityWorks(String id, String sort, int start, int count) {
        String referer = String.format(RexxarApi.refererCelebrity, id);
        return ((RexxarApi) getApi(BaseContants.BASE_URL_MOBILE, RexxarApi.class)).getCelebrityWorks(referer, id, sort, start, count);
    }

    @Override
    public Observable<CommonResult<Void, Photo>> rexxarGetCelebrityPhotos(String id, int start, int count) {
        String referer = String.format(RexxarApi.refererCelebrityAllPhotos, id);
        return ((RexxarApi) getApi(BaseContants.BASE_URL_MOBILE, RexxarApi.class)).getCelebrityPhoto(referer, id, start, count);
    }

    public Observable<String> getHtml(String baseUrl) {
        return ((JsoupApi) getApi(baseUrl, JsoupApi.class)).getHtml("");
    }
}
