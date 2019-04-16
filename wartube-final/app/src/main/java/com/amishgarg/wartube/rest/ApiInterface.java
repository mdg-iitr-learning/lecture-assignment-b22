package com.amishgarg.wartube.rest;

import com.amishgarg.wartube.Model.Notif;
import com.amishgarg.wartube.Model.User;
import com.amishgarg.wartube.Model.YoutubeModels.ChannelResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @GET("channels")
    Call<ChannelResponse> getSubs(@QueryMap Map<String, String> qMap);


    @FormUrlEncoded
    @POST("api/send")
    Call<Notif> sendUser(@Field("token") String token, @Field("title") String title, @Field("body") String body);
}
