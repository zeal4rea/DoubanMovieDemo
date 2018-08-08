package com.zeal4rea.doubanmoviedemo.bean;

import java.io.Serializable;
import java.util.Arrays;

public class Celebrity implements Serializable {
    public String id;
    public String name;
    public String name_en;
    public String alt;
    public String mobile_url;
    public Image avatars;
    public String summary;
    public String[] aka;
    public String[] aka_en;
    public String website;
    public String gender;
    public String birthday;
    public String born_place;
    public String[] professions;
    public Photo[] photos;
    public Subject[] works;

    @Override
    public String toString() {
        return "Celebrity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", name_en='" + name_en + '\'' +
                ", alt='" + alt + '\'' +
                ", mobile_url='" + mobile_url + '\'' +
                ", avatars=" + avatars +
                ", summary='" + summary + '\'' +
                ", aka=" + Arrays.toString(aka) +
                ", aka_en=" + Arrays.toString(aka_en) +
                ", website='" + website + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", born_place='" + born_place + '\'' +
                ", professions=" + Arrays.toString(professions) +
                ", photos=" + Arrays.toString(photos) +
                ", works=" + Arrays.toString(works) +
                '}';
    }
}
