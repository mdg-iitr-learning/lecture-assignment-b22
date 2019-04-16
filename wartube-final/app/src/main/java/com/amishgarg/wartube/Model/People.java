package com.amishgarg.wartube.Model;

import java.util.Map;

public class People {

    private String display_name;
    private String profile_pic;
    private Map<String, Boolean> posts;


    public People() {

    }

    public People(String display_name, String profile_pic, Map<String, Boolean> posts) {
        this.display_name = display_name;
        this.profile_pic = profile_pic;
        this.posts = posts;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public Map<String, Boolean> getPosts() {
        return posts;
    }

}

