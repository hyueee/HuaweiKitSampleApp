package com.example.huaweikitsampleapp;

public class UserModel {
    String email, username;

    public UserModel() {

    }

    public UserModel(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
