package com.zeal4rea.doubanmoviedemo.bean.api;

import java.io.Serializable;

public class WeeklySubject implements Serializable {
    public int rank;
    public int delta;

    @Override
    public String toString() {
        return "WeeklySubject{" +
                "rank=" + rank +
                ", delta=" + delta +
                '}';
    }
}
