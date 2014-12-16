package com.cryptic.imed.activity.prescription;

import android.os.Bundle;
import com.cryptic.imed.domain.Prescription;
import com.cryptic.imed.fragment.prescription.PrescriptionDetailsFragment;
import com.cryptic.imed.fragment.prescription.PrescriptionListFragment;
import com.google.inject.Inject;
import roboguice.activity.RoboFragmentActivity;

/**
 * @author sharafat
 */
public class PrescriptionDetailsActivity extends RoboFragmentActivity {
    @Inject
    private PrescriptionDetailsFragment prescriptionDetailsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Prescription selectedPrescription = (Prescription)
                getIntent().getSerializableExtra(PrescriptionListFragment.KEY_PRESCRIPTION);
        prescriptionDetailsFragment.setPrescription(selectedPrescription);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, prescriptionDetailsFragment).commit();
    }
}
