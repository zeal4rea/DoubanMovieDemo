package com.zeal4rea.doubanmoviedemo.bean;

import java.io.Serializable;

public class Rating implements Serializable {
    public int min;
    public int max;
    public float average;
    public int stars;

    @Override
    public String toString() {
        return "Rating{" +
                "min=" + min +
                ", max=" + max +
                ", average=" + average +
                ", stars=" + stars +
                '}';
    }
}
