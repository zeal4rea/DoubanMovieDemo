package com.zeal4rea.doubanmoviedemo.network;

import com.zeal4rea.doubanmoviedemo.network.api.CelebrityApi;
import com.zeal4rea.doubanmoviedemo.network.api.OtherApi;
import com.zeal4rea.doubanmoviedemo.network.api.SearchApi;
import com.zeal4rea.doubanmoviedemo.network.api.SubjectApi;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static SubjectApi subjectApi;
    private static CelebrityApi celebrityApi;
    private static SearchApi searchApi;
    private static OtherApi otherApi;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create();

    public static SubjectApi getSubjectApi() {
        if (subjectApi == null) {
            subjectApi = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BaseContants.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .build()
                    .create(SubjectApi.class);
        }
        return subjectApi;
    }

    public static CelebrityApi getCelebrityApi() {
        if (celebrityApi == null) {
            celebrityApi = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BaseContants.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .build()
                    .create(CelebrityApi.class);
        }
        return celebrityApi;
    }

    public static SearchApi getSearchApi() {
        if (searchApi == null) {
            searchApi = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BaseContants.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .build()
                    .create(SearchApi.class);
        }
        return searchApi;
    }

    public static OtherApi getOtherApi() {
        if (otherApi == null) {
            otherApi = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BaseContants.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .build()
                    .create(OtherApi.class);
        }
        return otherApi;
    }
}
