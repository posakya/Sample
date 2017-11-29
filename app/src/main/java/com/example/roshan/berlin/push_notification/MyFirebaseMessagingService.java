package com.example.roshan.berlin.push_notification;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.example.roshan.berlin.MainActivity;
import com.example.roshan.berlin.Notification;
import com.example.roshan.berlin.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by roshan on 6/3/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    String title;
    String desc;
    Context context;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

          Intent intent=
                  new Intent(this, Notification.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        title =remoteMessage.getNotification().getTitle();
        desc=remoteMessage.getNotification().getBody();
      //  String music=remoteMessage.getNotification().getSound();
//        intent.putExtra("Title",title);
//        intent.putExtra("Desc",desc);
//        System.out.println("Title :"+title);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationbuilder=new NotificationCompat.Builder(this);
        notificationbuilder.setContentTitle(remoteMessage.getNotification().getTitle());
        notificationbuilder.setContentText(Html.fromHtml(remoteMessage.getNotification().getBody()));
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationbuilder.setSound(alarmSound);

        notificationbuilder.setAutoCancel(true);
        notificationbuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationbuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationbuilder.build());

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
