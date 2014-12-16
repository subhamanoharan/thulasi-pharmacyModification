package com.cryptic.imed.activity.pharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.DashboardActivity;
import com.cryptic.imed.controller.PharmacyController;
import com.cryptic.imed.domain.Pharmacy;
import com.cryptic.imed.fragment.pharmacy.PharmacyListFragment;
import com.cryptic.imed.util.Validation;
import com.cryptic.imed.util.view.CompatibilityUtils;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.io.Serializable;

/**
 * @author sharafat
 */
@ContentView(R.layout.new_pharmacy)
public class AddEditPharmacyActivity extends RoboActivity {
    @Inject
    private PharmacyController pharmacyController;

    @InjectView(R.id.pharmacy_name_input)
    private EditText pharmacyNameInput;
    @InjectView(R.id.pharmacy_phone_input)
    private EditText pharmacyPhoneInput;
    @InjectView(R.id.pharmacy_address_input)
    private EditText pharmacyAddressInput;
    @InjectView(R.id.pharmacy_email_input)
    private EditText pharmacyEmailInput;
    @InjectView(R.id.pharmacy_website_input)
    private EditText pharmacyWebsiteInput;
    @InjectView(R.id.notes_input)
    private EditText notesInput;

    @InjectResource(R.string.required)
    private String required;
    @InjectResource(R.string.invalid_email)
    private String invalidEmailAddress;
    @InjectResource(R.string.invalid_url)
    private String invalidUrl;

    private Pharmacy pharmacy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preparePharmacy(getIntent().getSerializableExtra(PharmacyListFragment.KEY_PHARMACY));

        CompatibilityUtils.setHomeButtonEnabled(true, this);
    }

    private void preparePharmacy(Serializable pharmacyToBeEdited) {
        if (pharmacyToBeEdited == null) {
            pharmacy = new Pharmacy();
        } else {
            pharmacy = (Pharmacy) pharmacyToBeEdited;
            updateViewWithPharmacyDetails();
        }
    }

    private void updateViewWithPharmacyDetails() {
        pharmacyNameInput.setText(pharmacy.getName());
        pharmacyPhoneInput.setText(pharmacy.getPhone());
        pharmacyAddressInput.setText(pharmacy.getAddress());
        pharmacyEmailInput.setText(pharmacy.getEmail());
        pharmacyWebsiteInput.setText(pharmacy.getWebsite());
        notesInput.setText(pharmacy.getNotes());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }

        return false;
    }

    public void onSaveButtonClicked(View view) {
        if (!Validation.validateRequired(pharmacyNameInput, required)
                | !Validation.validateEmail(pharmacyEmailInput, invalidEmailAddress)
                | !Validation.validateUrl(pharmacyWebsiteInput, invalidUrl)) {
            return;
        }

        savePharmacy();

        startActivity(new Intent(this, PharmacyListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    private void savePharmacy() {
        pharmacy.setName(pharmacyNameInput.getText().toString());
        pharmacy.setPhone(pharmacyPhoneInput.getText().toString());
        pharmacy.setAddress(pharmacyAddressInput.getText().toString());
        pharmacy.setEmail(pharmacyEmailInput.getText().toString());
        pharmacy.setWebsite(pharmacyWebsiteInput.getText().toString());
        pharmacy.setNotes(notesInput.getText().toString());

        pharmacyController.save(pharmacy);
    }

    public void onCancelButtonClicked(View view) {
        finish();
    }
}
