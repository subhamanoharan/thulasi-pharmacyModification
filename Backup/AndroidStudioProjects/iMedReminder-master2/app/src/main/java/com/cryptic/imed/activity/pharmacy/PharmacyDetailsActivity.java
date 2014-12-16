package com.cryptic.imed.activity.pharmacy;

import android.os.Bundle;
import com.cryptic.imed.domain.Pharmacy;
import com.cryptic.imed.fragment.pharmacy.PharmacyDetailsFragment;
import com.cryptic.imed.fragment.pharmacy.PharmacyListFragment;
import com.google.inject.Inject;
import roboguice.activity.RoboFragmentActivity;

/**
 * @author sharafat
 */
public class PharmacyDetailsActivity extends RoboFragmentActivity {
    @Inject
    private PharmacyDetailsFragment pharmacyDetailsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Pharmacy selectedPharmacy = (Pharmacy) getIntent().getSerializableExtra(PharmacyListFragment.KEY_PHARMACY);
        pharmacyDetailsFragment.setPharmacy(selectedPharmacy);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, pharmacyDetailsFragment).commit();
    }
}
