package com.zeal4rea.doubanmoviedemo.bean.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BoxSubject implements Serializable {
    public int rank;
    public int box;
    @SerializedName(value = "new")
    public boolean isNew;
    public Subject subject;

    @Override
    public String toString() {
        return "BoxSubject{" +
                "rank=" + rank +
                ", box=" + box +
                ", isNew=" + isNew +
                ", subject=" + subject +
                '}';
    }
}
