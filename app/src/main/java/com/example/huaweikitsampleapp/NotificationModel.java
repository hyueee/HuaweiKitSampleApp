package com.example.huaweikitsampleapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationModel {
    public static final int NOTI_ID = 0;
    public static final String NOTI_SPECIAL_ID = "0";
    public static final String NOTI_TITLE = "title";
    public static final String NOTI_CONTENT = "content";
    public static final String NOTI_SENDER = "sender";
    public static final String NOTI_GAME = "game";
    public static final String NOTI_ROOM_NAME = "roomName";
    public static String room_selected = "";
    static DatabaseReference myRef;
    static PendingIntent pendingIntent;

    public static void showNotification(Context context, String id, String title, String content, String sender, String gameId, String roomName, Intent intent) {
        String NOTIFICATION_CHANNEL_ID = "com.example.huaweikitsampleapp";
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "TeamPlay App", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("TeamPlay App");
        notificationChannel.setLightColor(Color.GREEN);
        notificationChannel.enableLights(true);
        notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
        notificationChannel.enableVibration(true);
        notificationChannel.setSound(ringtoneUri, att);
        notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setSound(ringtoneUri)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_people);

        if (intent != null) {
            pendingIntent = PendingIntent.getActivity(context, NOTI_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent resultIntent = new Intent(context, ChatActivity.class);
                resultIntent.putExtra("roomId", id);
                resultIntent.putExtra("roomName", roomName);
                resultIntent.putExtra("gameId", gameId);
                resultIntent.putExtra("userId", userId);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntentWithParentStack(resultIntent);

                pendingIntent = stackBuilder.getPendingIntent(NOTI_ID, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(pendingIntent);
                Notification notification = builder.build();
                if (!sender.equals(userId) && !NotificationModel.room_selected.equals(id)) {
                    notificationManager.notify(id, NOTI_ID, notification);
                }
            }
        }

    }

}
