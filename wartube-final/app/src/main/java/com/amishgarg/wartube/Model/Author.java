package com.amishgarg.wartube.Model;

public class Author {

    private String display_name;
    private String uid;
    private String profile_pic;

    public Author() {

    }

    public Author(String display_name, String uid, String profile_pic) {
        this.display_name = display_name;
        this.uid = uid;
        this.profile_pic = profile_pic;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getUid() {
        return uid;
    }

    public String getProfile_pic() {
        return profile_pic;
    }
}
