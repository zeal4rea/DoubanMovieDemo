package com.zeal4rea.doubanmoviedemo.bean;

import java.util.Arrays;

public class Subject {
    public String id;
    public String title;
    public String original_title;
    public String[] aka;
    public String alt;
    public String mobile_url;
    public Rating rating;
    public int ratings_count;
    public int wish_count;
    public int collect_count;
    public int do_count;
    public Image images;
    public String subtype;
    public Celebrity[] directors;
    public Celebrity[] casts;
    public Celebrity[] writers;
    public String website;
    public String douban_site;
    public String[] pubdates;
    public String mainland_pubdate;
    public String pubdate;
    public String year;
    public String[] languages;
    public String[] durations;
    public String[] genres;
    public String[] countries;
    public String summary;
    public int comments_count;
    public int reviews_count;
    public int seasons_count;
    public int current_season;
    public int episodes_count;
    public String schedule_url;
    public String[] trailer_urls;
    public String[] clip_urls;
    public String[] blooper_urls;
    public Photo[] photos;
    public Review[] popular_reviews;

    @Override
    public String toString() {
        return "Subject{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", original_title='" + original_title + '\'' +
                ", aka=" + Arrays.toString(aka) +
                ", alt='" + alt + '\'' +
                ", mobile_url='" + mobile_url + '\'' +
                ", rating=" + rating +
                ", ratings_count=" + ratings_count +
                ", wish_count=" + wish_count +
                ", collect_count=" + collect_count +
                ", do_count=" + do_count +
                ", images=" + images +
                ", subtype='" + subtype + '\'' +
                ", directors=" + Arrays.toString(directors) +
                ", casts=" + Arrays.toString(casts) +
                ", writers=" + Arrays.toString(writers) +
                ", website='" + website + '\'' +
                ", douban_site='" + douban_site + '\'' +
                ", pubdates=" + Arrays.toString(pubdates) +
                ", mainland_pubdate='" + mainland_pubdate + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", year='" + year + '\'' +
                ", languages=" + Arrays.toString(languages) +
                ", durations=" + Arrays.toString(durations) +
                ", genres=" + Arrays.toString(genres) +
                ", countries=" + Arrays.toString(countries) +
                ", summary='" + summary + '\'' +
                ", comments_count=" + comments_count +
                ", reviews_count=" + reviews_count +
                ", seasons_count=" + seasons_count +
                ", current_season=" + current_season +
                ", episodes_count=" + episodes_count +
                ", schedule_url='" + schedule_url + '\'' +
                ", trailer_urls=" + Arrays.toString(trailer_urls) +
                ", clip_urls=" + Arrays.toString(clip_urls) +
                ", blooper_urls=" + Arrays.toString(blooper_urls) +
                ", photos=" + Arrays.toString(photos) +
                ", popular_reviews=" + Arrays.toString(popular_reviews) +
                '}';
    }
}
