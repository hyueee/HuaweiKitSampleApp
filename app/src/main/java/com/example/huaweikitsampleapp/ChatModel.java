package com.example.huaweikitsampleapp;

public class ChatModel {
    String sender, message, date, time, timeStamp, userName, image, top;

    public ChatModel() {

    }

    public ChatModel(String sender, String message, String date, String time, String timeStamp, String userName, String image, String top) {
        this.sender = sender;
        this.message = message;
        this.date = date;
        this.time = time;
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.image = image;
        this.top = top;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public String getImage() {
        return image;
    }

    public String getTop() {
        return top;
    }
}

