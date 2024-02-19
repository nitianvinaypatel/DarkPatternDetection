package com.darkpattern.detection.model;

public class User {
    private String userId;
    private String image;
    private String name;
    private String email;

    public User() {
    }

    public User(String userId,String image, String name, String email) {
        this.userId = userId;
        this.image = image;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String userId) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
