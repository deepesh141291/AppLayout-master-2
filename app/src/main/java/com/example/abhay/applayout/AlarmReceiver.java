package com.example.abhay.applayout;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
        System.out.println("alarm recevier called ");
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        createNotification(context,"Times Up","You hav reached","Alert");
    }

    private void createNotification(Context context, String s, String s1, String s2) {
        PendingIntent notificIntent = PendingIntent.getActivity(context,0, new Intent(context,MainActivity.class).putExtra("key","1"),0);

        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                .setContentTitle(s)
                .setTicker(s1)
                .setContentText(s2);
        mbuilder.setContentIntent(notificIntent);



        mbuilder.setDefaults(android.support.v7.app.NotificationCompat.DEFAULT_SOUND);

        mbuilder.setAutoCancel(true);

        NotificationManager mnotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        mnotificationManager.notify(1, mbuilder.build());
    }


}