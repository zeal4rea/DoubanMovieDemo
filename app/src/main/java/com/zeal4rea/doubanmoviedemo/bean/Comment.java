package com.zeal4rea.doubanmoviedemo.bean;

import java.io.Serializable;

public class Comment implements Serializable {
    public String id;
    public String created_at;
    public String subject_id;
    public User author;
    public String content;
    public Rating rating;
    public int useful_count;

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", created_at='" + created_at + '\'' +
                ", subject_id='" + subject_id + '\'' +
                ", author=" + author +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", useful_count=" + useful_count +
                '}';
    }
}
