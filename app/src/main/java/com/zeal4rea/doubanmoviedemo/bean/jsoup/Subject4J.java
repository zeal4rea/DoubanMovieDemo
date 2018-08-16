package com.zeal4rea.doubanmoviedemo.bean.jsoup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Subject4J implements Serializable {
    public String id;
    public String title;
    public String meta;
    public String summary;
    public String cover;
    public String rating;
    public String voter;
    public List<Celebrity4J> celebrities = new ArrayList<>();
    public List<Photo4J> photos = new ArrayList<>();
    public List<Comment4J> comments = new ArrayList<>();
    public List<Review4J> reviews = new ArrayList<>();

    public static Subject4J createBasic(String id, String title, String meta, String summary, String cover, String rating, String voter) {
        Subject4J s = new Subject4J();
        s.id = id;
        s.title = title;
        s.meta = meta;
        s.summary = summary;
        s.cover = cover;
        s.rating = rating;
        s.voter = voter;
        return s;
    }

    @Override
    public String toString() {
        return "Subject4J{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", meta='" + meta + '\'' +
                ", summary='" + summary + '\'' +
                ", cover='" + cover + '\'' +
                ", rating='" + rating + '\'' +
                ", voter='" + voter + '\'' +
                ", celebrities=" + celebrities +
                ", photos=" + photos +
                ", comments=" + comments +
                ", reviews=" + reviews +
                '}';
    }
}
