package com.zeal4rea.doubanmoviedemo.bean;

public class Review {
    public String id;
    public String title;
    public String alt;
    public String created_at;
    public String updated_at;
    public String subject_id;
    public User author;
    public String summary;
    public Rating rating;
    public int useful_count;
    public int useless_count;
    public int comments_count;

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", alt='" + alt + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", subject_id='" + subject_id + '\'' +
                ", author=" + author +
                ", summary='" + summary + '\'' +
                ", rating=" + rating +
                ", useful_count=" + useful_count +
                ", useless_count=" + useless_count +
                ", comments_count=" + comments_count +
                '}';
    }
}
