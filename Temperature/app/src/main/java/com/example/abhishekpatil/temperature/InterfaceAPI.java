package com.example.abhishekpatil.temperature;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceAPI {
    @GET("weather")
    Call<Weather> getdata(@Query("appid") String apikey,
                          @Query("q") String cityName);
}
