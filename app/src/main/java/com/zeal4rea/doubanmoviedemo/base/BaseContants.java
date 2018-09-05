package com.zeal4rea.doubanmoviedemo.base;

public class BaseContants {
    public final static String BASE_URL = "https://douban.com";
    public final static String BASE_URL_MOBILE = "https://m.douban.com";
    public final static String BASE_URL_API = "http://api.douban.com";
    public final static String APPLICATION_TAG = BaseApplication.getInstance().getPackageName();
    public final static String NETWORK_TAG = "network_log";
    public final static String DEBUGING_TAG = "debuging_log";
    public final static String NIGHT_MODE = "night_mode";
    public final static String USER_AGENT = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, votes Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36";
    public final static int DEFAULT_COUNT = 20;
    public final static int DEFAULT_COUNT_GALLERY = 21;

    public final static int TYPE_GALLERY_SUBJECT = -1;
    public final static int TYPE_GALLERY_CELEBRITY = -2;

    public final static String[] SORT_CELEBRITY_WORK = {"vote", "time"};
}
