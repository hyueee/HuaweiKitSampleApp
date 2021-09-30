package com.example.huaweikitsampleapp;
public class User {

    public String username, email, id, firstTime;

    public User(){

    }

    public User( String username,String email, String id, String firstTime){
        this.username=username;
        this.email=email;
        this.id=id;
        this.firstTime = firstTime;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }
}
