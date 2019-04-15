package com.sachan.prateek.weather;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit_Builder {
    private static Retrofit retrofit;
    private static final String BASEURL="http://api.openweathermap.org";
    static Retrofit getRetrofit() {
        if (retrofit==null){
            retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASEURL)
                    .build();
        }
        return retrofit;
    }
}
