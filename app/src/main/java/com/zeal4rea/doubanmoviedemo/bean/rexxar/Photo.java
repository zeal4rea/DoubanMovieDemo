package com.zeal4rea.doubanmoviedemo.bean.rexxar;

import java.io.Serializable;

public class Photo implements Serializable {
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
}
