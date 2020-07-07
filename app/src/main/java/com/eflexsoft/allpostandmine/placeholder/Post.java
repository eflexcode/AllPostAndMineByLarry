package com.eflexsoft.allpostandmine.placeholder;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class Post {

     private String posterId;
     private String title;
     private String body;
     private String image;
     private String date;

    public Post() {
    }

    public Post(String posterId, String title, String body, String image, String date) {
        this.posterId = posterId;
        this.title = title;
        this.body = body;
        this.image = image;
        this.date = date;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
