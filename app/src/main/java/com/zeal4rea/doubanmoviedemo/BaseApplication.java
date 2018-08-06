package com.zeal4rea.doubanmoviedemo;

import android.app.Application;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        CustomOnCrash.install(this);
    }
}
