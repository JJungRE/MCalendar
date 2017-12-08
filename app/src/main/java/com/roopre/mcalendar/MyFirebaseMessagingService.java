package com.roopre.mcalendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by jjungre on 2017. 10. 30..
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Intent intent;
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String seq = remoteMessage.getData().get("seq");

        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "seq: " + seq);

        intent = new Intent(this, MainActivity.class);
        intent.putExtra("message", message);
        intent.putExtra("title", title);
        intent.putExtra("seq", seq);
        if (Se_Application.Localdb.get_dataB("push")) {
            sendNewBenefit(title, message);
        }
    }

    private void sendNewBenefit(String title, String message) {
        int notifyID = 1236;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker(title)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notifyID, mBuilder.build());
    }
}
