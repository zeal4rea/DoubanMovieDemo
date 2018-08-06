package com.zeal4rea.doubanmoviedemo.bean;

import com.google.gson.annotations.SerializedName;

public class BoxSubject {
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
