package com.zeal4rea.doubanmoviedemo;

import android.app.Application;

import com.zeal4rea.doubanmoviedemo.util.crash.CustomOnCrash;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
//        CustomOnCrash.install(this);
//        CaocConfig.Builder.create().apply();
    }
}
