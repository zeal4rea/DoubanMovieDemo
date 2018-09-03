package com.zeal4rea.doubanmoviedemo.bean.rexxar;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Photo implements Serializable, Parcelable {
    public String id;
    public Image image;
    public User author;
    public String create_time;
    public String description;
    public String owner_uri;
    public String reaction_type;
    public String sharing_url;
    public String type;
    public String uri;
    public String url;
    public int position;
    public int donate_count;
    public int donate_money;
    public int donate_user_count;
    public int likers_count;
    public int reactions_count;
    public int reshares_count;
    public int collections_count;
    public int comments_count;
    public boolean allow_comment;
    public boolean allow_donate;
    public boolean is_collected;
    public boolean is_donated;
    //public String[] recent_donate_senders;

    protected Photo(Parcel in) {
        id = in.readString();
        image = (Image) in.readSerializable();
        author = (User) in.readSerializable();
        create_time = in.readString();
        description = in.readString();
        owner_uri = in.readString();
        reaction_type = in.readString();
        sharing_url = in.readString();
        type = in.readString();
        uri = in.readString();
        url = in.readString();
        position = in.readInt();
        donate_count = in.readInt();
        donate_money = in.readInt();
        donate_user_count = in.readInt();
        likers_count = in.readInt();
        reactions_count = in.readInt();
        reshares_count = in.readInt();
        collections_count = in.readInt();
        comments_count = in.readInt();
        allow_comment = in.readByte() != 0;
        allow_donate = in.readByte() != 0;
        is_collected = in.readByte() != 0;
        is_donated = in.readByte() != 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeSerializable(image);
        parcel.writeSerializable(author);
        parcel.writeString(create_time);
        parcel.writeString(description);
        parcel.writeString(owner_uri);
        parcel.writeString(reaction_type);
        parcel.writeString(sharing_url);
        parcel.writeString(type);
        parcel.writeString(uri);
        parcel.writeString(url);
        parcel.writeInt(position);
        parcel.writeInt(donate_count);
        parcel.writeInt(donate_money);
        parcel.writeInt(donate_user_count);
        parcel.writeInt(likers_count);
        parcel.writeInt(reactions_count);
        parcel.writeInt(reshares_count);
        parcel.writeInt(collections_count);
        parcel.writeInt(comments_count);
        parcel.writeByte((byte) (allow_comment ? 1 : 0));
        parcel.writeByte((byte) (allow_donate ? 1 : 0));
        parcel.writeByte((byte) (is_collected ? 1 : 0));
        parcel.writeByte((byte) (is_donated ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
