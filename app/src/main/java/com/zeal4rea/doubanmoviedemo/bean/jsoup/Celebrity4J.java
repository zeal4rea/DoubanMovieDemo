package com.zeal4rea.doubanmoviedemo.bean.jsoup;

import java.io.Serializable;

public class Celebrity4J implements Serializable {
    public String id;
    public String name;
    public String photoUrl;
    public String role;
    public static Celebrity4J create(String id, String name, String photoUrl, String role) {
        Celebrity4J c = new Celebrity4J();
        c.id = id;
        c.name = name;
        c.photoUrl = photoUrl;
        c.role = role;
        return c;
    }
}
