package com.example.huaweikitsampleapp;

public class RoomModel {

    String name, numPlayer, id, server, time, currentPlayer, status;

    public RoomModel() {
    }

    public RoomModel(String name, String numPlayer, String id, String server, String time, String currentPlayer, String status) {
        this.name = name;
        this.numPlayer = numPlayer;
        this.id = id;
        this.server = server;
        this.time = time;
        this.currentPlayer = currentPlayer;
        this.status = status;
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

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getStatus() {
        return status;
    }
}

