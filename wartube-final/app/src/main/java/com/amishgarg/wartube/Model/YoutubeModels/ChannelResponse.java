package com.amishgarg.wartube.Model.YoutubeModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChannelResponse {

    @SerializedName("items")
    private List<Channel> channels;

    public ChannelResponse(List<Channel> channels) {
        this.channels = channels;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }
}
