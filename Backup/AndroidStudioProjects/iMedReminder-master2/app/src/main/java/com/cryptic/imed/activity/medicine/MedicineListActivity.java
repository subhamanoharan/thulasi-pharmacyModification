package com.cryptic.imed.activity.medicine;

import android.os.Bundle;
import android.widget.LinearLayout;
import com.cryptic.imed.R;
import com.cryptic.imed.fragment.medicine.MedicineDetailsFragment;
import com.cryptic.imed.fragment.medicine.MedicineListFragment;
import com.cryptic.imed.util.view.ViewUtils;
import com.google.inject.Inject;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import javax.annotation.Nullable;

/**
 * @author sharafat
 */
@ContentView(R.layout.list)
public class MedicineListActivity extends RoboFragmentActivity {
    @Inject
    private MedicineListFragment medicineListFragment;
    @Inject
    private MedicineDetailsFragment medicineDetailsFragment;

    @InjectView(R.id.details_container)
    @Nullable
    private LinearLayout detailsContainer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.addListFragmentToLayout(this, detailsContainer, medicineListFragment, medicineDetailsFragment);
    }
}
