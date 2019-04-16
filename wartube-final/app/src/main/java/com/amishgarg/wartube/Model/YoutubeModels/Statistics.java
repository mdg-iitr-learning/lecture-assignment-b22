package com.amishgarg.wartube.Model.YoutubeModels;

import com.google.gson.annotations.SerializedName;

public class Statistics {

    @SerializedName("subscriberCount")
    private int subscriberCount;

    public Statistics(int subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public int getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(int subscriberCount) {
        this.subscriberCount = subscriberCount;
    }
}
