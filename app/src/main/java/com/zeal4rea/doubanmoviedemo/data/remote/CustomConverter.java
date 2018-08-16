package com.zeal4rea.doubanmoviedemo.data.remote;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class CustomConverter<T> implements Converter<ResponseBody, T> {
    @Override
    public T convert(ResponseBody value) throws IOException {
        //todo
        return (T) value.string();
    }
}
