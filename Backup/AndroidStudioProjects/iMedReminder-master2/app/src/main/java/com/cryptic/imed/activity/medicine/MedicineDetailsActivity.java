package com.cryptic.imed.activity.medicine;

import android.os.Bundle;
import com.cryptic.imed.domain.Medicine;
import com.cryptic.imed.fragment.medicine.MedicineDetailsFragment;
import com.cryptic.imed.fragment.medicine.MedicineListFragment;
import com.google.inject.Inject;
import roboguice.activity.RoboFragmentActivity;

/**
 * @author sharafat
 */
public class MedicineDetailsActivity extends RoboFragmentActivity {
    @Inject
    private MedicineDetailsFragment medicineDetailsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Medicine selectedMedicine = (Medicine) getIntent().getSerializableExtra(MedicineListFragment.KEY_MEDICINE);
        medicineDetailsFragment.setMedicine(selectedMedicine);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, medicineDetailsFragment).commit();
    }
}
