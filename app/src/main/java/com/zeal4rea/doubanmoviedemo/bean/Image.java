package com.zeal4rea.doubanmoviedemo.bean;

import java.io.Serializable;

public class Image implements Serializable {
    public String large;
    public String medium;
    public String small;

    @Override
    public String toString() {
        return "Image{" +
                "large='" + large + '\'' +
                ", medium='" + medium + '\'' +
                ", small='" + small + '\'' +
                '}';
    }
}
