package com.zeal4rea.doubanmoviedemo.data.remote;

import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Comment4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Subject4J;
import com.zeal4rea.doubanmoviedemo.data.remote.jsoup.JsoupParser;
import com.zeal4rea.doubanmoviedemo.util.Utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class CustomConverterFactory extends Converter.Factory {
    private static final CustomConverterFactory INSTANCE = new CustomConverterFactory();
    private CustomConverterFactory() {}
    public static CustomConverterFactory create() {
        return INSTANCE;
    }
    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        //Utils.logWithDebugingTag("converter---type:" + type.toString());
        if (type == String.class) {
            return new Converter<ResponseBody, String>() {
                @Override
                public String convert(ResponseBody value) throws IOException {
                    return value.string();
                }
            };
        } else if (type == Subject4J.class) {
            return new Converter<ResponseBody, Subject4J>() {
                @Override
                public Subject4J convert(ResponseBody value) throws IOException {
                    String html = value.string();
                    return JsoupParser.parseSubjectDetail(html);
                }
            };
        } else if (type.toString().equals(new TypeToken<List<Comment4J>>(){}.getType().toString())) {
            return new Converter<ResponseBody, List<Comment4J>>() {
                @Override
                public List<Comment4J> convert(ResponseBody value) throws IOException {
                    String html = value.string();
                    return JsoupParser.parseComments(html);
                }
            };
        } else if (type.toString().equals(new TypeToken<List<Review4J>>(){}.getType().toString())) {
            return new Converter<ResponseBody, List<Review4J>>() {
                @Override
                public List<Review4J> convert(ResponseBody value) throws IOException {
                    String html = value.string();
                    return JsoupParser.parseReviews(html);
                }
            };
        }
        return null;
    }
}
