package com.example.huaweikitsampleapp;

public class RoomModel {

    String name, numPlayer, id, server, time, language, currentPlayer, status, requestUser, requestDate;

    public RoomModel() {
    }

    public RoomModel(String name, String numPlayer, String id, String server, String time, String language, String currentPlayer, String status, String requestUser, String requestDate) {
        this.name = name;
        this.numPlayer = numPlayer;
        this.id = id;
        this.server = server;
        this.time = time;
        this.language = language;
        this.currentPlayer = currentPlayer;
        this.status = status;
        this.requestUser = requestUser;
        this.requestDate = requestDate;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getNumPlayer() {
        return numPlayer;
    }

    public String getServer() {
        return server;
    }

    public String getTime() {
        return time;
    }

    public String getLanguage() {
        return language;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getStatus() {
        return status;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public String getRequestDate() {
        return requestDate;
    }
}

