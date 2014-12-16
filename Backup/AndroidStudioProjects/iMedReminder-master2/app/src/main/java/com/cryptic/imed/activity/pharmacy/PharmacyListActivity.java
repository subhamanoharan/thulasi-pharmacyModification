package com.cryptic.imed.activity.pharmacy;

import android.os.Bundle;
import android.widget.LinearLayout;
import com.cryptic.imed.R;
import com.cryptic.imed.fragment.pharmacy.PharmacyDetailsFragment;
import com.cryptic.imed.fragment.pharmacy.PharmacyListFragment;
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
public class PharmacyListActivity extends RoboFragmentActivity {
    @Inject
    private PharmacyListFragment pharmacyListFragment;
    @Inject
    private PharmacyDetailsFragment pharmacyDetailsFragment;

    @InjectView(R.id.details_container)
    @Nullable
    private LinearLayout detailsContainer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.addListFragmentToLayout(this, detailsContainer, pharmacyListFragment, pharmacyDetailsFragment);
    }
}
