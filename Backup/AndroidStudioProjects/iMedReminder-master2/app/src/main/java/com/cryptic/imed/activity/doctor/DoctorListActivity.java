package com.cryptic.imed.activity.doctor;

import android.os.Bundle;
import android.widget.LinearLayout;
import com.cryptic.imed.R;
import com.cryptic.imed.fragment.doctor.DoctorDetailsFragment;
import com.cryptic.imed.fragment.doctor.DoctorListFragment;
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
public class DoctorListActivity extends RoboFragmentActivity {
    @Inject
    private DoctorListFragment doctorListFragment;
    @Inject
    private DoctorDetailsFragment doctorDetailsFragment;

    @InjectView(R.id.details_container)
    @Nullable
    private LinearLayout detailsContainer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.addListFragmentToLayout(this, detailsContainer, doctorListFragment, doctorDetailsFragment);
    }
}
