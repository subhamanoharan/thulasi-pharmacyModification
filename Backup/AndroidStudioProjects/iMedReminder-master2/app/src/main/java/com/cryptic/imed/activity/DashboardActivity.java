package com.cryptic.imed.activity;

import android.content.Intent;
import android.view.View;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.appointment.AppointmentListActivity;
import com.cryptic.imed.activity.doctor.DoctorListActivity;
import com.cryptic.imed.activity.medicine.MedicineListActivity;
import com.cryptic.imed.activity.pharmacy.PharmacyListActivity;
import com.cryptic.imed.activity.prescription.PrescriptionListActivity;
import com.cryptic.imed.util.view.IndefinitelyProgressingTask;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;

@ContentView(R.layout.dashboard)
public class DashboardActivity extends RoboActivity {
    @InjectResource(R.string.loading)
    private String loadingMessage;

    public void onPrescriptionsClicked(View view) {
        startActivity(PrescriptionListActivity.class);
    }

    public void onMedicinesClicked(View view) {
        startActivity(MedicineListActivity.class);
    }

    public void onDoctorsClicked(View view) {
        startActivity(DoctorListActivity.class);
    }

    public void onPharmaciesClicked(View view) {
        startActivity(PharmacyListActivity.class);
    }

    public void onAppointmentsClicked(View view) {
        startActivity(AppointmentListActivity.class);
    }

    public void onSchedulesClicked(View view) {
        startActivity(ScheduleActivity.class);
    }

    private void startActivity(final Class<?> clazz) {
        new IndefinitelyProgressingTask<Void>(this, loadingMessage,
                new IndefinitelyProgressingTask.OnTaskExecutionListener<Void>() {
                    @Override
                    public Void execute() {
                        startActivity(new Intent(DashboardActivity.this, clazz));
                        return null;
                    }

                    @Override
                    public void onSuccess(Void result) {
                        //ignore
                    }

                    @Override
                    public void onException(Exception e) {
                        //ignore
                    }
                }).execute();
    }
}
