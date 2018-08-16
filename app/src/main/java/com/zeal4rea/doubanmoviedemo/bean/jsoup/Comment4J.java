package com.zeal4rea.doubanmoviedemo.bean.jsoup;

import java.io.Serializable;

public class Comment4J implements Serializable {
    public String content;
    public String date;
    public User4J user;
    public String rating;
    public String votes;
    public static Comment4J create(String content, String date, User4J user, String rating, String votes) {
        Comment4J c = new Comment4J();
        c.content = content;
        c.date = date;
        c.user = user;
        c.rating = rating;
        c.votes = votes;
        return c;
    }

    @Override
    public String toString() {
        return "Comment4J{" +
                "content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", user=" + user +
                ", rating='" + rating + '\'' +
                ", votes='" + votes + '\'' +
                '}';
    }
}
