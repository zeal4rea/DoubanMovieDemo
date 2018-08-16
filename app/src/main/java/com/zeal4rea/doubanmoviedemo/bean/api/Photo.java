package com.zeal4rea.doubanmoviedemo.bean.api;

import java.io.Serializable;

public class Photo implements Serializable {
    public String id;
    public String subject_id;
    public String alt;
    public String icon;
    public String image;
    public String thumb;
    public String cover;
    public String created_at;
    public String desc;
    public User author;
    public String album_id;
    public String album_title;
    public String album_url;
    public String next_photo;
    public String prev_photo;
    public int position;
    public int comments_count;
    public int photos_count;

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", subject_id='" + subject_id + '\'' +
                ", alt='" + alt + '\'' +
                ", icon='" + icon + '\'' +
                ", image='" + image + '\'' +
                ", thumb='" + thumb + '\'' +
                ", cover='" + cover + '\'' +
                ", created_at='" + created_at + '\'' +
                ", desc='" + desc + '\'' +
                ", author=" + author +
                ", album_id='" + album_id + '\'' +
                ", album_title='" + album_title + '\'' +
                ", album_url='" + album_url + '\'' +
                ", next_photo='" + next_photo + '\'' +
                ", prev_photo='" + prev_photo + '\'' +
                ", position=" + position +
                ", comments_count=" + comments_count +
                ", photos_count=" + photos_count +
                '}';
    }
}
