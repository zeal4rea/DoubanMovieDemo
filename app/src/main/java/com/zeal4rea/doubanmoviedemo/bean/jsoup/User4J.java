package com.zeal4rea.doubanmoviedemo.bean.jsoup;

import java.io.Serializable;

public class User4J implements Serializable {
    public String name;
    public String iconUrl;
    public static User4J create(String name, String iconUrl) {
        User4J u = new User4J();
        u.name = name;
        u.iconUrl = iconUrl;
        return u;
    }
}
