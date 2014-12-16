package com.cryptic.imed.activity.appointment;

import android.os.Bundle;
import com.cryptic.imed.domain.Appointment;
import com.cryptic.imed.fragment.appointment.AppointmentDetailsFragment;
import com.cryptic.imed.fragment.appointment.AppointmentListFragment;
import com.google.inject.Inject;
import roboguice.activity.RoboFragmentActivity;

/**
 * @author sharafat
 */
public class AppointmentDetailsActivity extends RoboFragmentActivity {
    @Inject
    private AppointmentDetailsFragment appointmentDetailsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Appointment selectedAppointment = (Appointment)
                getIntent().getSerializableExtra(AppointmentListFragment.KEY_APPOINTMENT);
        appointmentDetailsFragment.setAppointment(selectedAppointment);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, appointmentDetailsFragment).commit();
    }
}
