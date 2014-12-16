package com.cryptic.imed.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.cryptic.imed.R;
import com.cryptic.imed.domain.*;
import com.j256.ormlite.support.ConnectionSource;

/**
 * @author sharafat
 *
 * Note: To enable all debug messages for all ORMLite classes, use the following command:
 *          adb shell setprop log.tag.ORMLite DEBUG
 */
public class DbHelper extends AbstractDbHelper {
    private static final String DB_NAME = "imedreminder.db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        createTables(connectionSource,
                Medicine.class,
                Doctor.class,
                Pharmacy.class,
                Appointment.class,
                Dosage.class,
                PrescriptionMedicine.class,
                Prescription.class,
                DosagesTaken.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }
}
