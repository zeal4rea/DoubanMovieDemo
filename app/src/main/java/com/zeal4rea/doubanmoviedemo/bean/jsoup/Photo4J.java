package com.zeal4rea.doubanmoviedemo.bean.jsoup;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Photo4J implements Serializable, Parcelable {
    public String small;
    public String medium;
    public String large;

    public Photo4J() {
    }

    protected Photo4J(Parcel in) {
        small = in.readString();
        medium = in.readString();
        large = in.readString();
    }

    public static final Creator<Photo4J> CREATOR = new Creator<Photo4J>() {
        @Override
        public Photo4J createFromParcel(Parcel in) {
            return new Photo4J(in);
        }

        @Override
        public Photo4J[] newArray(int size) {
            return new Photo4J[size];
        }
    };

    public static Photo4J create(String small, String medium, String large) {
        Photo4J p = new Photo4J();
        p.small = small;
        p.medium = medium;
        p.large = large;
        return p;
    }

    public static Photo4J create(String id) {
        String url = "https://img1.doubanio.com/view/photo/%s/public/p%s.webp";
        Photo4J p = new Photo4J();
        p.small = String.format(url, "s", id);
        p.medium = String.format(url, "m", id);
        p.large = String.format(url, "l", id);
        return p;
    }

    @Override
    public String toString() {
        return "Photo4J{" +
                "small='" + small + '\'' +
                ", medium='" + medium + '\'' +
                ", large='" + large + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(small);
        parcel.writeString(medium);
        parcel.writeString(large);
    }
}
