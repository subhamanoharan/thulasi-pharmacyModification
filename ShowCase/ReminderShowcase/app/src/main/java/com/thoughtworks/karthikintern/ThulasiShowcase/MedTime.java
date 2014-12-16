package com.thoughtworks.karthikintern.ThulasiShowcase;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

/**
 * Created by karthikintern on 9/16/14.
 */
public class MedTime extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Build a Notification for Med Reminder
        NotificationCompat.Builder notification;
        PendingIntent pIntent;
        NotificationManager manager;
        Intent resultIntent;
        TaskStackBuilder stackBuilder;
        notification = new NotificationCompat.Builder(MedTime.this);
        //Title for Notification
        //("Thulasi Pharmacy on Mobile");
        notification.setContentTitle("Thulasi Pharmacy");
        //Message in the Notification
        notification.setContentText("Time to take your Medicine");
        //Alert shown when Notification is received
        notification.setTicker("Thulasi Pharmacy  - Notification");
        //Icon to be set on Notification
        notification.setSmallIcon(R.drawable.ic_launcher);
        //Creating new Stack Builder
        stackBuilder = TaskStackBuilder.create(MedTime.this);
        stackBuilder.addParentStack(NotifResult.class);
        //Intent which is opened when notification is clicked
        resultIntent = new Intent(MedTime.this, NotifResult.class);
        stackBuilder.addNextIntent(resultIntent);
        pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pIntent);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }

}

