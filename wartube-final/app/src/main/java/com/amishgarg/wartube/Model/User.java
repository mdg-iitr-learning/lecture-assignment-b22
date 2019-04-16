package com.amishgarg.wartube.Model;

import java.util.List;

public class User {

    private String display_name;
    private String profile_pic;
    private List<Post> posts;
    private String token;

    public User(String display_name, String profile_pic, List<Post> posts, String token) {
        this.display_name = display_name;
        this.profile_pic = profile_pic;
        this.posts = posts;
        this.token = token;
    }

    public User() {
    }

    public String getToken() {
        return token;
    }
}
