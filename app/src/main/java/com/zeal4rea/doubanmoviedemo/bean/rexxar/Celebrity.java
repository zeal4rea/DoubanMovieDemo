package com.zeal4rea.doubanmoviedemo.bean.rexxar;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Celebrity implements Serializable {
    public String id;
    public String name;
    public String latin_name;
    public String cover_url;
    public Image avatar;
    public String character;
    public String title;
    @SerializedName("abstract")
    public String abstractInfo;
    public String type;
    public String uri;
    public String url;
    public String sharing_url;
    public User author;
    public User user;
    public String[] roles;


    public String known_for;
    public String works_count;
    public String intro;
    public String info;
    public String nominations_count;
    public String album;
    public String awards_count;
    public String fans_counts;
    public String has_fanned;
    public String header_bg_color;
    public String body_bg_color;
    public String[] latest_works;
    public String[] related_celebrities;
    public String[] awards;
    public String[] criminations;

    public String role;

    public static class Image {
        public String large;
        public String normal;
    }
}
