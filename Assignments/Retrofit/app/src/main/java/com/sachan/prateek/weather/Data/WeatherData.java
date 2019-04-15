package com.sachan.prateek.weather.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherData {

    @SerializedName("coord")
    @Expose
    private Coordinates coord;
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("visibility")
    @Expose
    private double visibility;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("clouds")
    @Expose
    private CloudData clouds;
    @SerializedName("dt")
    @Expose
    private double dt;
    @SerializedName("sys")
    @Expose
    private Sys sys;
    @SerializedName("id")
    @Expose
    private double id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose

    private double cod;

    public Coordinates getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public double getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public CloudData getClouds() {
        return clouds;
    }

    public double getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public double getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCod() {
        return cod;
    }
}