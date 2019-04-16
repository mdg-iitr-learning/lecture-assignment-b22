package com.amishgarg.wartube.Model.YoutubeModels;

import com.google.gson.annotations.SerializedName;

public class Channel {

    @SerializedName("statistics")
    private Statistics statistics;

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public Channel(Statistics statistics) {

        this.statistics = statistics;
    }
}
