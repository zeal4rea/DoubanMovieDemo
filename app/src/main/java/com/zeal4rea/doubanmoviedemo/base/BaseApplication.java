package com.zeal4rea.doubanmoviedemo.base;

import android.app.Application;
import android.content.Context;

import com.zeal4rea.doubanmoviedemo.data.DataRepository;
import com.zeal4rea.doubanmoviedemo.data.local.LocalDataSource;
import com.zeal4rea.doubanmoviedemo.data.remote.RemoteDataSource;

public class BaseApplication extends Application {
    private static BaseApplication INSTANCE;

    public static BaseApplication getInstance() {
        return INSTANCE;
    }

    public static Context getContext() {
        return INSTANCE.getApplicationContext();
    }

    public static DataRepository getDataRepository() {
        return DataRepository.getInstance(LocalDataSource.getInstance(), RemoteDataSource.getInstance());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        init();
    }

    private void init() {
    }
}
