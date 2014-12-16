package com.cryptic.imed.activity.doctor;

import android.os.Bundle;
import com.cryptic.imed.domain.Doctor;
import com.cryptic.imed.fragment.doctor.DoctorDetailsFragment;
import com.cryptic.imed.fragment.doctor.DoctorListFragment;
import com.google.inject.Inject;
import roboguice.activity.RoboFragmentActivity;

/**
 * @author sharafat
 */
public class DoctorDetailsActivity extends RoboFragmentActivity {
    @Inject
    private DoctorDetailsFragment doctorDetailsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Doctor selectedDoctor = (Doctor) getIntent().getSerializableExtra(DoctorListFragment.KEY_DOCTOR);
        doctorDetailsFragment.setDoctor(selectedDoctor);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, doctorDetailsFragment).commit();
    }
}
