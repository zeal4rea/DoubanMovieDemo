package com.zeal4rea.doubanmoviedemo.bean.jsoup;

import java.io.Serializable;

public class Photo4J implements Serializable {
    public String small;
    public String medium;
    public String large;
    public static Photo4J create(String small, String medium, String large) {
        Photo4J p = new Photo4J();
        p.small = small;
        p.medium = medium;
        p.large = large;
        return p;
    }

    public static Photo4J create(String id) {
        String url = "https://img1.doubanio.com/view/photo/%s/public/p%s.webp";
        Photo4J p = new Photo4J();
        p.small = String.format(url, "s", id);
        p.medium = String.format(url, "m", id);
        p.large = String.format(url, "l", id);
        return p;
    }

    @Override
    public String toString() {
        return "Photo4J{" +
                "small='" + small + '\'' +
                ", medium='" + medium + '\'' +
                ", large='" + large + '\'' +
                '}';
    }
}
