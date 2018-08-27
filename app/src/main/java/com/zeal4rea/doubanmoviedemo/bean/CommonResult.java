package com.zeal4rea.doubanmoviedemo.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

public class CommonResult<A, B> implements Serializable {
    public int start;
    public int count;
    public int total;
    public String date;
    public String query;
    public String tag;
    public String title;
    @SerializedName(value = "subject", alternate = {"celebrity"})
    public A belong;
    @SerializedName(value = "subjects", alternate = {"photos", "reviews", "comments", "works", "interests"})
    public B[] results;

    @Override
    public String toString() {
        return "CommonResult{" +
                "start=" + start +
                ", count=" + count +
                ", total=" + total +
                ", date='" + date + '\'' +
                ", query='" + query + '\'' +
                ", tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                ", belong=" + belong +
                ", results=" + Arrays.toString(results) +
                '}';
    }
}
