package com.example.huaweikitsampleapp;
public class User {

    public String username, email, id;

    public User(){

    }

    public User( String username,String email, String id){
        this.username=username;
        this.email=email;
        this.id=id;
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
}
