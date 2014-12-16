package com.cryptic.imed.activity.prescription;

import android.os.Bundle;
import android.widget.LinearLayout;
import com.cryptic.imed.R;
import com.cryptic.imed.fragment.prescription.PrescriptionDetailsFragment;
import com.cryptic.imed.fragment.prescription.PrescriptionListFragment;
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
public class PrescriptionListActivity extends RoboFragmentActivity {
    @Inject
    private PrescriptionListFragment prescriptionListFragment;
    @Inject
    private PrescriptionDetailsFragment prescriptionDetailsFragment;

    @InjectView(R.id.details_container)
    @Nullable
    private LinearLayout detailsContainer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.addListFragmentToLayout(this, detailsContainer, prescriptionListFragment, prescriptionDetailsFragment);
    }
}
