package com.cryptic.imed.domain.enums;

import android.app.Application;
import com.cryptic.imed.R;
import com.google.inject.Inject;

/**
 * @author sharafat
 */
public enum TimeIntervalUnit {
    HOURS   (R.string.time_interval_hours),
    MINUTES (R.string.time_interval_minutes),
    DAYS    (R.string.time_interval_days);

    @Inject
    private static Application application;

    private final int userFriendlyNameResourceId;

    private TimeIntervalUnit(int userFriendlyNameResourceId) {
        this.userFriendlyNameResourceId = userFriendlyNameResourceId;
    }

    public int getUserFriendlyNameResourceId() {
        return userFriendlyNameResourceId;
    }

    public String getUserFriendlyName() {
        return application.getString(userFriendlyNameResourceId);
    }

    @Override
    public String toString() {
        return getUserFriendlyName();
    }
}
