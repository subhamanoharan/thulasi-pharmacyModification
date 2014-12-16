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
public class StockLow extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Build a Notification for Low Stock of Medicine
        NotifyStocker();
        finish();
    }

    public void NotifyStocker() {
        NotificationCompat.Builder notification;
        PendingIntent pIntent;
        NotificationManager manager;
        Intent resultIntent;
        TaskStackBuilder stackBuilder;
        notification = new NotificationCompat.Builder(StockLow.this);
        //Title for Notification
        //("Thulasi Pharmacy on Mobile");
        notification.setContentTitle("Thulasi Pharmacy on Mobile");
        //Message in the Notification
        notification.setContentText("Stock of Medication(s) is low");
        //Alert shown when Notification is received
        notification.setTicker("Thulasi Pharmacy  - Notification");
        //Icon to be set on Notification
        notification.setSmallIcon(R.drawable.ic_launcher);
        //Creating new Stack Builder
        stackBuilder = TaskStackBuilder.create(StockLow.this);
        stackBuilder.addParentStack(PresView.class);
        //Intent which is opened when notification is clicked
        resultIntent = new Intent(StockLow.this, NotifResult.class);
        stackBuilder.addNextIntent(resultIntent);
        pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pIntent);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }
}
