package com.cryptic.imed.service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import com.cryptic.imed.controller.ScheduleController;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * @author sharafat
 */
@Singleton
public class AlarmService {
    public static final String KEY_MEDICINE_SCHEDULE = "medicine_schedule";

    private static final int REQ_CODE_PENDING_INTENT_MEDICINE_ALARM = 100;
    private static final Logger log = LoggerFactory.getLogger(AlarmService.class);

    @Inject
    private Application application;
    @Inject
    private AlarmManager alarmManager;
    @Inject
    private ScheduleController scheduleController;

    public void setMedicineReminderAlarm() {
        Calendar closestUpcomingScheduleTime = scheduleController.getClosestUpcomingReminderScheduleTime();
        log.debug("closestUpcomingScheduleTime: {}", closestUpcomingScheduleTime);

        Intent serviceIntent = new Intent(application, ReminderNotificationService.class);
        serviceIntent.putExtra(KEY_MEDICINE_SCHEDULE, closestUpcomingScheduleTime);

        PendingIntent operation = PendingIntent.getService(application, REQ_CODE_PENDING_INTENT_MEDICINE_ALARM,
                serviceIntent, 0);

        alarmManager.cancel(operation);
        if (closestUpcomingScheduleTime != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, closestUpcomingScheduleTime.getTimeInMillis(), operation);
            log.info("alarm set to: {}", closestUpcomingScheduleTime.getTime());
        }
    }
}
