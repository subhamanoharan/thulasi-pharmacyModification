package com.cryptic.imed.domain.enums;

import android.app.Application;
import android.graphics.drawable.Drawable;
import com.cryptic.imed.R;
import com.google.inject.Inject;

/**
 * @author sharafat
 */
public enum MedicineType {
    TABLET    (R.string.tablet,    R.drawable.ic_med_tablet),
    CAPSULE   (R.string.capsule,   R.drawable.ic_med_capsule),
    SYRUP     (R.string.syrup,     R.drawable.ic_med_syrup),
    INJECTION (R.string.injection, R.drawable.ic_med_injection),
    SALINE    (R.string.saline,    R.drawable.ic_med_saline),
    DROP      (R.string.drop,      R.drawable.ic_med_drop),
    CREAM     (R.string.cream,     R.drawable.ic_med_cream),
    INHALER   (R.string.inhaler,   R.drawable.ic_med_inhaler),
    SPRAY     (R.string.spray,     R.drawable.ic_med_spray),
    OTHER     (R.string.other,     R.drawable.ic_med_other);

    @Inject
    private static Application application;

    private final int userFriendlyNameResourceId;
    private final int iconResourceId;

    private MedicineType(int userFriendlyNameResourceId, int iconResourceId) {
        this.userFriendlyNameResourceId = userFriendlyNameResourceId;
        this.iconResourceId = iconResourceId;
    }

    public int getUserFriendlyNameResourceId() {
        return userFriendlyNameResourceId;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public String getUserFriendlyName() {
        return application.getString(userFriendlyNameResourceId);
    }

    public Drawable getIcon() {
        return application.getResources().getDrawable(iconResourceId);
    }

    @Override
    public String toString() {
        return getUserFriendlyName();
    }
}
