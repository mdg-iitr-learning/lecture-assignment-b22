package com.amishgarg.wartube.Model;

public class Comments {

    private Object timestamp;
    private int num_comments;

    public Comments(Object timestamp, int num_comments) {
        this.timestamp = timestamp;
        this.num_comments = num_comments;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public int getNum_comments() {
        return num_comments;
    }

    public void setNum_comments(int num_comments) {
        this.num_comments = num_comments;
    }
}
