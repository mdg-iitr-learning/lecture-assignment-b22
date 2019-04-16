package com.amishgarg.wartube.Model;

public class Likes {

    private Object timestamp;
    private int num_likes;

    public Likes(Object timestamp, int num_likes) {
        this.timestamp = timestamp;
        this.num_likes = num_likes;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public int getNum_likes() {
        return num_likes;
    }

    public void setNum_likes(int num_likes) {
        this.num_likes = num_likes;
    }
}
