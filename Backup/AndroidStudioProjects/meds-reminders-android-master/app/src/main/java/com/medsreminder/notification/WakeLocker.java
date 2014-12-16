package com.medsreminder.notification;

import com.medsreminder.ui.MedNotification;

import android.content.Context;
import android.os.PowerManager;

//Used to wake phone when notification starts
public abstract class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    @SuppressWarnings("deprecation")
	public static void acquire(Context ctx) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, MedNotification.WAKE_TAG);
        wakeLock.acquire();
    }

    public static void release() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
}