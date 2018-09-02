package com.zeal4rea.doubanmoviedemo.bean.rexxar;

import java.io.Serializable;

public class CelebrityWork implements Serializable {
    public String id;
    public String card_subtitle;
    public boolean has_linewatch;
    public boolean is_released;
    public String null_rating_reason;
    public String released_date;
    public String sharing_url;
    public String subtype;
    public String title;
    public String type;
    public String uri;
    public String url;
    public String year;
    public Rating rating;
    public Picture pic;
    //public String color_scheme;
    public PersonWithName[] actors;
    public PersonWithName[] directors;
    public String[] genres;
    public String[] pubdate;

    public static class PersonWithName implements Serializable {
        public String name;
    }
}
