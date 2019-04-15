package com.sachan.prateek.weather;

import com.sachan.prateek.weather.Data.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Weather_Service {
    @GET("/data/2.5/weather")
    Call<WeatherData> getWeatherData(@Query("APPID") String apiKey, @Query("zip") String zip);
}
