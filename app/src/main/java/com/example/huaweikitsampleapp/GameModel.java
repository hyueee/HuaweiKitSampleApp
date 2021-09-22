package com.example.huaweikitsampleapp;

public class GameModel {
    String name, details, id;

    public GameModel() {
    }

    public GameModel(String name, String details, String id) {
        this.name = name;
        this.details = details;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getId() {
        return id;
    }
}
