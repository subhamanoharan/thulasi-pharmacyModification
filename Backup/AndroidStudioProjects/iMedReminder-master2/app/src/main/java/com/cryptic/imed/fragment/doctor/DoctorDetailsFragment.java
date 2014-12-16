package com.cryptic.imed.fragment.doctor;

import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.DashboardActivity;
import com.cryptic.imed.activity.doctor.AddEditDoctorActivity;
import com.cryptic.imed.activity.doctor.DoctorListActivity;
import com.cryptic.imed.controller.DoctorController;
import com.cryptic.imed.domain.Doctor;
import com.cryptic.imed.util.StringUtils;
import com.cryptic.imed.util.photo.util.ImageUtils;
import com.cryptic.imed.util.view.CompatibilityUtils;
import com.cryptic.imed.util.view.DualPaneUtils;
import com.google.inject.Inject;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import javax.annotation.Nullable;

/**
 * @author sharafat
 */
public class DoctorDetailsFragment extends RoboFragment {
    @Inject
    private Application application;
    @Inject
    private DoctorController doctorController;

    @InjectFragment(R.id.list_container)
    @Nullable
    private DoctorListFragment doctorListFragment;

    @InjectView(R.id.doctor_details_view)
    private ScrollView doctorDetailsView;
    @InjectView(R.id.doc_name)
    private TextView docNameTextView;
    @InjectView(R.id.doc_photo)
    private ImageView docPhotoImageView;
    @InjectView(R.id.doc_address)
    private TextView docAddressTextView;
    @InjectView(R.id.doc_phone)
    private TextView docPhoneTextView;
    @InjectView(R.id.doc_email)
    private TextView docEmailTextView;
    @InjectView(R.id.doc_website)
    private TextView docWebsiteTextView;
    @InjectView(R.id.notes)
    private TextView notesTextView;

    @InjectResource(R.string.not_available)
    private String notAvailable;
    @InjectResource(R.drawable.ic_default_photo)
    private Drawable defaultPhoto;

    private boolean dualPanel;
    private Doctor doctor;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CompatibilityUtils.setHomeButtonEnabled(true, getActivity());

        dualPanel = DualPaneUtils.isDualPane(getActivity(), R.id.list_container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_doctor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateView();
    }

    public void updateView() {
        if (doctor != null) {
            docNameTextView.setText(doctor.getName());
            docAddressTextView.setText(StringUtils.getNonEmptyString(doctor.getAddress(), notAvailable));
            docPhoneTextView.setText(StringUtils.getNonEmptyString(doctor.getPhone(), notAvailable));
            docEmailTextView.setText(StringUtils.getNonEmptyString(doctor.getEmail(), notAvailable));
            docWebsiteTextView.setText(StringUtils.getNonEmptyString(doctor.getWebsite(), notAvailable));
            notesTextView.setText(StringUtils.getNonEmptyString(doctor.getNotes(), notAvailable));
            docPhotoImageView.setImageBitmap(ImageUtils.getNonEmptyImage(doctor.getPhoto(), defaultPhoto));

            doctorDetailsView.setVisibility(View.VISIBLE);
            setHasOptionsMenu(true);
        } else {
            doctorDetailsView.setVisibility(View.GONE);
            setHasOptionsMenu(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_delete_options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent intent = new Intent(application, AddEditDoctorActivity.class);
                intent.putExtra(DoctorListFragment.KEY_DOCTOR, doctor);
                startActivity(intent);
                break;
            case R.id.menu_delete:
                if (dualPanel) {
                    assert doctorListFragment != null;
                    doctorListFragment.deleteDoctorAndUpdateView(doctor);
                } else {
                    doctor.setDeleted(true);
                    doctorController.save(doctor);

                    Intent doctorListActivityIntent = new Intent(application, DoctorListActivity.class);
                    startActivity(doctorListActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().finish();
                }
                break;
            case android.R.id.home:
                startActivity(new Intent(application, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }

        return false;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
