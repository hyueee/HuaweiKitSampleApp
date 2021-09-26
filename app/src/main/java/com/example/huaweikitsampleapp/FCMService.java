package com.example.huaweikitsampleapp;

import androidx.annotation.NonNull;

import com.example.huaweikitsampleapp.NotificationModel;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> dataReceive = remoteMessage.getData();

        if (dataReceive != null) {

            NotificationModel.showNotification(this,
                    dataReceive.get(NotificationModel.NOTI_SPECIAL_ID),
                    dataReceive.get(NotificationModel.NOTI_TITLE),
                    dataReceive.get(NotificationModel.NOTI_CONTENT),
                    dataReceive.get(NotificationModel.NOTI_SENDER),
                    dataReceive.get(NotificationModel.NOTI_GAME),
                    dataReceive.get(NotificationModel.NOTI_ROOM_NAME),
                    null);
        }
    }
}