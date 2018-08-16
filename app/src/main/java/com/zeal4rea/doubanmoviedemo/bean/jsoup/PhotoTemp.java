package com.zeal4rea.doubanmoviedemo.bean.jsoup;

public class PhotoTemp {
    public String url;
    public String n_comments;
    public String description;
    public String imageurl;

    @Override
    public String toString() {
        return "PhotoTemp{" +
                "url='" + url + '\'' +
                ", n_comments='" + n_comments + '\'' +
                ", description='" + description + '\'' +
                ", imageurl='" + imageurl + '\'' +
                '}';
    }
}
