package com.zeal4rea.doubanmoviedemo.bean;

public class Rating {
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
