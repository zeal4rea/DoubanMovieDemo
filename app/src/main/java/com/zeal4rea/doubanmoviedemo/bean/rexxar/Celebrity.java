package com.zeal4rea.doubanmoviedemo.bean.rexxar;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Celebrity implements Serializable {
    public String id;
    public String name;
    public String latin_name;
    public String cover_url;
    public Picture avatar;
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


    //public String[] known_for;
    public String intro;
    public String info;
    public Album album;
    public String header_bg_color;
    public String body_bg_color;
    public int works_count;
    public int nominations_count;
    public int awards_count;
    public int fans_counts;
    public boolean has_fanned;
    public CelebrityWork[] latest_works;
    public RelatedCelebrity[] related_celebrities;
    public Award[] awards;
    //public String[] criminations;

    public String role;

    public static class RelatedCelebrity implements Serializable {
        public Celebrity celebrity;
        public String info;
    }

    public static class Award implements Serializable {
        public Category category;
        public Ceremony ceremony;
        public boolean is_won;
        public String type;
        public CelebrityWork movie;

        public static class Category implements Serializable {
            public String title;
        }

        public static class Ceremony implements Serializable {
            public String sharing_url;
            public String title;
            public String url;
            public Picture pic;
            public String uri;
            public String type;
            public String id;
            public int number;
            public int year;
        }
    }

    public static class Album implements Serializable {
        public Photo[] photos;
        public int total;
    }
}
