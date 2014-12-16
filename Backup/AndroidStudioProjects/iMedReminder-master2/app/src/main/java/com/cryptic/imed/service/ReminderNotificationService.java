package com.cryptic.imed.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.ReminderDetailsActivity;
import roboguice.RoboGuice;
import roboguice.inject.InjectResource;

import java.util.Calendar;

/**
 * @author sharafat
 */
public class ReminderNotificationService extends IntentService {
    private static final int REMINDER_NOTIFICATION_ID = 8271;

    @InjectResource(R.string.app_name)
    private String appName;
    @InjectResource(R.string.notification_ticker_text)
    private String tickerText;
    @InjectResource(R.string.medicine_schedule_reminder)
    private String medicineScheduleReminder;

    public ReminderNotificationService() {
        super("ReminderNotificationService");
        RoboGuice.injectMembers(this, this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Calendar scheduleTime = (Calendar) intent.getSerializableExtra(AlarmService.KEY_MEDICINE_SCHEDULE);
        notifyUser(scheduleTime);
    }

    @SuppressWarnings("deprecation")
    private void notifyUser(Calendar scheduleTime) {
        PendingIntent contentIntent = getIntentForStartingReminderDetailsActivityOnTappingNotification(scheduleTime);

        Notification reminderNotification = new Notification(R.drawable.ic_launcher, tickerText, System.currentTimeMillis());
        reminderNotification.setLatestEventInfo(this, appName, medicineScheduleReminder, contentIntent);
        reminderNotification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(REMINDER_NOTIFICATION_ID, reminderNotification);
    }

    private PendingIntent getIntentForStartingReminderDetailsActivityOnTappingNotification(Calendar scheduleTime) {
        Intent notificationIntent = new Intent(this, ReminderDetailsActivity.class);
        notificationIntent.putExtra(AlarmService.KEY_MEDICINE_SCHEDULE, scheduleTime);
        return PendingIntent.getActivity(this, 0, notificationIntent, 0);
    }
}
