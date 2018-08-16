package com.zeal4rea.doubanmoviedemo.data.remote.jsoup;

import com.zeal4rea.doubanmoviedemo.bean.jsoup.Celebrity4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Comment4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Photo4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Subject4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.User4J;
import com.zeal4rea.doubanmoviedemo.util.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class JsoupParser {
    public static Subject4J parseSubjectDetail(String html) {
        Document document = Jsoup.parse(html);
        Element card = document.select("body > div.page > div.card").first();
        String id = card.select("section.subject-mark").first().attr("data-id");
        String title = card.select("h1.title").text();
        String summary = card.select("section.subject-intro > div.bd > p").first().text().trim();
        String cover = card.select("section.subject-info > div.right > a > img.cover").first().attr("data-src");
        String meta = card.select("section.subject-info > div.left > p.meta").first().text().trim();
        String rating = "";
        String voter = "";
        if (!card.select("section.subject-info > div.left > p.rating > span").isEmpty()) {
            rating = card.select("section.subject-info > div.left > p.rating > span.rating-stars").first().attr("data-rating");
            voter = Utils.regexMatch("\\d+", card.select("section.subject-info > div.left > p.rating > span:not(.rating-stars)").first().text())[0];
        }
        Subject4J subject = Subject4J.createBasic(
                id,
                title,
                meta,
                summary,
                cover,
                rating,
                voter
        );
        return subject;
    }

    public static List<Celebrity4J> parseCelebrities(String html) {
        Document document = Jsoup.parse(html);
        List<Celebrity4J> celebrities = new ArrayList<>();
        for (Element box : document.select("#celebrities > div > ul > li")) {
            String id = Utils.regexMatch("\\d+", box.select("a").first().attr("href"))[0];
            String name = box.select("a > span.item-title.name").first().text();
            String photoUrl = Utils.regexMatch("(\\()(\\S+)(\\))", box.select("a > div").first().attr("style"))[2];
            String role = box.select("a > span.item-title.role").first().text();
            celebrities.add(Celebrity4J.create(
                    id,
                    name,
                    photoUrl,
                    role
            ));
        }
        return celebrities;
    }

    public static List<Photo4J> parsePhotos(String html) {
        Document document = Jsoup.parse(html);
        List<Photo4J> photos = new ArrayList<>();
        for (Element e : document.select("ul#photolist > li > a")) {
            try {
                String raw = e.attr("style");
                String[] urlGroups = Utils.regexMatch("(\\()(\\S+)(\\))", raw);
                String[] arr = Utils.regexMatch("(\\S+photo\\/)\\S(\\/public\\S+)", urlGroups[2]);
                String front = arr[1];
                String rear = arr[2];
                photos.add(Photo4J.create(
                        front + "s" + rear,
                        front + "m" + rear,
                        front + "l" + rear
                ));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return photos;
    }

    public static List<Comment4J> parseComments(String html) {
        Document document = Jsoup.parse(html);
        List<Comment4J> comments = new ArrayList<>();
        for (Element box : document.select("div#comments > div.comment-item")) {
            try {
                String content = box.select("div.comment > p > span").first().text();
                String date = box.select("span.comment-time").first().text().trim();
                String votes = box.select("span.votes").first().text();
                Element ratingEle = box.select("span.rating").first();
                String rating = "";
                if (ratingEle != null) {
                    rating = Utils.regexMatch("\\d+", ratingEle.attr("class"))[0];
                }
                User4J user = User4J.create(
                        box.select("div.avatar > a").first().attr("title"),
                        box.select("div.avatar > a > img").attr("src"));

                Comment4J c = Comment4J.create(
                        content,
                        date,
                        user,
                        rating,
                        votes
                );
                comments.add(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return comments;
    }

    public static List<Review4J> parseReviews(String html) {
        Document document = Jsoup.parse(html);
        List<Review4J> reviews = new ArrayList<>();
        for (Element box : document.select("section.review-list > div > ul > li")) {
            try {
                String id = Utils.regexMatch("\\d+", box.select("a").first().attr("href"))[0];
                String title = box.select("a > h3").first().text();
                String content = box.select("a > p").first().text().trim();
                String useful = Utils.regexMatch("\\d+", box.select("a > div.info").first().text().trim())[0];
                String rating = box.select("a > div > span.rating-stars").first().attr("data-rating");
                User4J user = User4J.create(null, box.select("a > div > img").first().attr("src"));
                reviews.add(Review4J.create(
                        id,
                        title,
                        content,
                        useful,
                        "",
                        rating,
                        user
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return reviews;
    }
}
