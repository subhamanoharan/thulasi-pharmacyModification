package com.cryptic.imed.activity;

import android.os.Bundle;
import com.cryptic.imed.controller.ScheduleController;
import com.cryptic.imed.domain.PrescriptionMedicine;
import com.cryptic.imed.service.AlarmService;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;

import java.util.Calendar;
import java.util.List;

/**
 * @author sharafat
 */
public class ReminderDetailsActivity extends RoboActivity {
    @Inject
    private ScheduleController scheduleController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar scheduleTime = (Calendar) getIntent().getSerializableExtra(AlarmService.KEY_MEDICINE_SCHEDULE);
        List<PrescriptionMedicine> prescriptionMedicines = scheduleController.list(scheduleTime);

        //TODO
    }
}
