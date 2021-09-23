package com.example.huaweikitsampleapp;

public class RoomModel {

    String name, numPlayer, id, server, time, currentPlayer;

    public RoomModel() {
    }

    public RoomModel(String name, String numPlayer, String id, String server, String time, String currentPlayer) {
        this.name = name;
        this.numPlayer = numPlayer;
        this.id = id;
        this.server = server;
        this.time = time;
        this.currentPlayer = currentPlayer;
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
}

