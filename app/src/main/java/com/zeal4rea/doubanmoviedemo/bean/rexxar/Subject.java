package com.zeal4rea.doubanmoviedemo.bean.rexxar;

import java.io.Serializable;

public class Subject implements Serializable {
    public String id;
    public Image cover;
    public Image.ImageInfo cover_img;
    public String title;
    public String latin_title;
    public String desc;
    public ExtraInfo extra;
    public User author;
    public String created_at;
    public String uri;
    public String url;
    public String sharing_url;
    public String type;
    public String subtype;
    public String is_followed;
    public String followed_count;
    public String header_bg_color;
    //public String color_scheme;
    public Tag[] tags;
    //public String[] modules;

    public static class ExtraInfo implements Serializable {
        // String[] info;
        public String short_info;
        public String header_img;
        public String has_linewatch;
        public String year;
        public RatingGroup rating_group;

        public static class RatingGroup implements Serializable {
            public Rating rating;
            public String null_rating_reason;
        }
    }

    public static class Tag implements Serializable {
        public String id;
        public String name;
        public String uri;
        public String url;
        public String is_channel;
    }
}
