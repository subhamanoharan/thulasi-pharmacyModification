package com.cryptic.imed.domain.enums;

import android.app.Application;
import com.cryptic.imed.R;
import com.google.inject.Inject;

/**
 * @author sharafat
 */
public enum AppointmentType {
    DOCTOR   (R.string.appointment_with_doctor),
    PHARMACY (R.string.appointment_with_pharmacy),
    OTHER    (R.string.appointment_with_other);

    @Inject
    private static Application application;

    private final int userFriendlyNameResourceId;

    private AppointmentType(int userFriendlyNameResourceId) {
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
