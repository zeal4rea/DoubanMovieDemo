package com.zeal4rea.doubanmoviedemo.bean.jsoup;

import java.io.Serializable;

public class Review4J implements Serializable {
    public String id;
    public String title;
    public String content;
    public String useful;
    public String useless;
    public String rating;
    public User4J user;
    public static Review4J create(String id, String title, String content, String useful, String useless, String rating, User4J user) {
        Review4J r = new Review4J();
        r.id = id;
        r.title = title;
        r.content = content;
        r.useful = useful;
        r.useless = useless;
        r.rating = rating;
        r.user = user;
        return r;
    }

    @Override
    public String toString() {
        return "Review4J{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", useful='" + useful + '\'' +
                ", useless='" + useless + '\'' +
                ", rating='" + rating + '\'' +
                ", user=" + user +
                '}';
    }
}
