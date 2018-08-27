package com.zeal4rea.doubanmoviedemo.bean.rexxar;

import java.io.Serializable;

public class Comment implements Serializable {
    public String id;
    public String comment;
    public Rating rating;
    public User user;
    public String create_time;
    public String uri;
    public String sharing_url;
    public String is_voted;
    public String vote_count;
    public String status;
    //public String[] platforms;
}
