package com.sachan.prateek.weather.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CloudData {
    @SerializedName("all")
    @Expose
    private int all;

    public int getAll() {
        return all;
    }
}
