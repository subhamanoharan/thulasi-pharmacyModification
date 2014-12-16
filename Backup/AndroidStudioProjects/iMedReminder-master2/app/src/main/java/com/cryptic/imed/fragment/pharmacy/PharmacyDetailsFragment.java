package com.cryptic.imed.fragment.pharmacy;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ScrollView;
import android.widget.TextView;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.DashboardActivity;
import com.cryptic.imed.activity.pharmacy.AddEditPharmacyActivity;
import com.cryptic.imed.activity.pharmacy.PharmacyListActivity;
import com.cryptic.imed.controller.PharmacyController;
import com.cryptic.imed.domain.Pharmacy;
import com.cryptic.imed.util.StringUtils;
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
public class PharmacyDetailsFragment extends RoboFragment {
    @Inject
    private Application application;
    @Inject
    private PharmacyController pharmacyController;

    @InjectFragment(R.id.list_container)
    @Nullable
    private PharmacyListFragment pharmacyListFragment;

    @InjectView(R.id.pharmacy_details_view)
    private ScrollView pharmacyDetailsView;
    @InjectView(R.id.pharmacy_name)
    private TextView pharmacyNameTextView;
    @InjectView(R.id.pharmacy_address)
    private TextView pharmacyAddressTextView;
    @InjectView(R.id.pharmacy_phone)
    private TextView pharmacyPhoneTextView;
    @InjectView(R.id.pharmacy_email)
    private TextView pharmacyEmailTextView;
    @InjectView(R.id.pharmacy_website)
    private TextView pharmacyWebsiteTextView;
    @InjectView(R.id.notes)
    private TextView notesTextView;

    @InjectResource(R.string.not_available)
    private String notAvailable;

    private boolean dualPanel;
    private Pharmacy pharmacy;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CompatibilityUtils.setHomeButtonEnabled(true, getActivity());

        dualPanel = DualPaneUtils.isDualPane(getActivity(), R.id.list_container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_pharmacy, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateView();
    }

    public void updateView() {
        if (pharmacy != null) {
            pharmacyNameTextView.setText(pharmacy.getName());
            pharmacyAddressTextView.setText(StringUtils.getNonEmptyString(pharmacy.getAddress(), notAvailable));
            pharmacyPhoneTextView.setText(StringUtils.getNonEmptyString(pharmacy.getPhone(), notAvailable));
            pharmacyEmailTextView.setText(StringUtils.getNonEmptyString(pharmacy.getEmail(), notAvailable));
            pharmacyWebsiteTextView.setText(StringUtils.getNonEmptyString(pharmacy.getWebsite(), notAvailable));
            notesTextView.setText(StringUtils.getNonEmptyString(pharmacy.getNotes(), notAvailable));

            pharmacyDetailsView.setVisibility(View.VISIBLE);
            setHasOptionsMenu(true);
        } else {
            pharmacyDetailsView.setVisibility(View.GONE);
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
                Intent intent = new Intent(application, AddEditPharmacyActivity.class);
                intent.putExtra(PharmacyListFragment.KEY_PHARMACY, pharmacy);
                startActivity(intent);
                break;
            case R.id.menu_delete:
                if (dualPanel) {
                    assert pharmacyListFragment != null;
                    pharmacyListFragment.deletePharmacyAndUpdateView(pharmacy);
                } else {
                    pharmacy.setDeleted(true);
                    pharmacyController.save(pharmacy);

                    Intent pharmacyListActivityIntent = new Intent(application, PharmacyListActivity.class);
                    startActivity(pharmacyListActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().finish();
                }
                break;
            case android.R.id.home:
                startActivity(new Intent(application, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }

        return false;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }
}
