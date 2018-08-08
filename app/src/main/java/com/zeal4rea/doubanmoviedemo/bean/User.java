package com.zeal4rea.doubanmoviedemo.bean;

import java.io.Serializable;

public class User implements Serializable {
    public String id;
    public String name;
    public String uid;
    public String signature;
    public String alt;
    public String avatar;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", signature='" + signature + '\'' +
                ", alt='" + alt + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
