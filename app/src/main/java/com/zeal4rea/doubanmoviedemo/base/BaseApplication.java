package com.zeal4rea.doubanmoviedemo.base;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.zeal4rea.doubanmoviedemo.data.DataRepository;
import com.zeal4rea.doubanmoviedemo.data.local.LocalDataSource;
import com.zeal4rea.doubanmoviedemo.data.remote.RemoteDataSource;
import com.zeal4rea.doubanmoviedemo.util.Utils;

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
        boolean nightMode = Utils.sharePreferenceGetBoolean(BaseContants.NIGHT_MODE, false);
        AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }
}
