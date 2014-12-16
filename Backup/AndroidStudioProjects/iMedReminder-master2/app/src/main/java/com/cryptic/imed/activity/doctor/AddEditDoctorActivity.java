package com.cryptic.imed.activity.doctor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.DashboardActivity;
import com.cryptic.imed.common.Constants;
import com.cryptic.imed.controller.DoctorController;
import com.cryptic.imed.domain.Doctor;
import com.cryptic.imed.fragment.doctor.DoctorListFragment;
import com.cryptic.imed.util.Validation;
import com.cryptic.imed.util.photo.camera.CameraUnavailableException;
import com.cryptic.imed.util.photo.camera.OnPhotoTakeListener;
import com.cryptic.imed.util.photo.camera.PhotoTaker;
import com.cryptic.imed.util.photo.util.ImageUtils;
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
@ContentView(R.layout.new_doctor)
public class AddEditDoctorActivity extends RoboActivity {
    @Inject
    private DoctorController doctorController;
    @Inject
    private PhotoTaker photoTaker;

    @InjectView(R.id.doc_name_input)
    private EditText docNameInput;
    @InjectView(R.id.doc_phone_input)
    private EditText docPhoneInput;
    @InjectView(R.id.doc_address_input)
    private EditText docAddressInput;
    @InjectView(R.id.doc_email_input)
    private EditText docEmailInput;
    @InjectView(R.id.doc_website_input)
    private EditText docWebsiteInput;
    @InjectView(R.id.notes_input)
    private EditText notesInput;
    @InjectView(R.id.take_photo_btn)
    private ImageButton takePhotoButton;

    @InjectResource(R.array.doctor_photo_taking_options)
    private String[] photoTakingOptions;
    @InjectResource(R.string.add_doctor_photo)
    private String addDoctorPhoto;
    @InjectResource(R.string.remove_photo)
    private String removePhoto;
    @InjectResource(R.string.required)
    private String required;
    @InjectResource(R.string.invalid_email)
    private String invalidEmailAddress;
    @InjectResource(R.string.invalid_url)
    private String invalidUrl;

    private AlertDialog addDoctorPhotoDialog;
    private OnPhotoTakeListener onPhotoTakeListener;

    private Doctor doctor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPhotoTakerOptions();
        createAddDoctorPhotoDialog();
        setOnPhotoTakeListener();
        registerForContextMenu(takePhotoButton);
        prepareDoctor(getIntent().getSerializableExtra(DoctorListFragment.KEY_DOCTOR));

        CompatibilityUtils.setHomeButtonEnabled(true, this);
    }

    private void setPhotoTakerOptions() {
        photoTaker.setAspectXForCropping(1);
        photoTaker.setAspectYForCropping(1);
        photoTaker.setImageWidthAfterCropping(Constants.PHOTO_SIZE);
        photoTaker.setImageHeightAfterCropping(Constants.PHOTO_SIZE);
    }

    private void createAddDoctorPhotoDialog() {
        addDoctorPhotoDialog = new AlertDialog.Builder(this).setTitle(addDoctorPhoto)
                .setItems(photoTakingOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedOptionIndex) {
                        switch (selectedOptionIndex) {
                            case 0: //take from camera
                                try {
                                    photoTaker.takePhotoFromCamera();
                                } catch (CameraUnavailableException e) {
                                    new AlertDialog.Builder(AddEditDoctorActivity.this)
                                            .setMessage(R.string.camera_unavailable).create().show();
                                    takePhotoButton.setEnabled(false);
                                }
                                break;
                            case 1: //pick from gallery
                                photoTaker.pickImageFromGallery();
                                break;
                        }
                    }
                }).create();
    }

    private void setOnPhotoTakeListener() {
        onPhotoTakeListener = new OnPhotoTakeListener() {
            @Override
            public void onPhotoTaken(Bitmap photo) {
                if (photo != null) {
                    doctor.setPhoto(ImageUtils.getByteArray(photo));
                    takePhotoButton.setImageBitmap(photo);
                }
            }
        };
    }

    private void prepareDoctor(Serializable doctorToBeEdited) {
        if (doctorToBeEdited == null) {
            doctor = new Doctor();
        } else {
            doctor = (Doctor) doctorToBeEdited;
            updateViewWithDoctorDetails();
        }
    }

    private void updateViewWithDoctorDetails() {
        docNameInput.setText(doctor.getName());
        docPhoneInput.setText(doctor.getPhone());
        docAddressInput.setText(doctor.getAddress());
        docEmailInput.setText(doctor.getEmail());
        docWebsiteInput.setText(doctor.getWebsite());
        notesInput.setText(doctor.getNotes());
        if (doctor.getPhoto() != null) {
            takePhotoButton.setImageBitmap(ImageUtils.getBitmap(doctor.getPhoto()));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v == takePhotoButton && doctor.getPhoto() != null) {
            menu.add(removePhoto);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(removePhoto)) {
            doctor.setPhoto(null);
            takePhotoButton.setImageResource(R.drawable.btn_take_photo);
            return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoTaker.onActivityResult(requestCode, resultCode, data, onPhotoTakeListener);
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

    public void onTakePhotoButtonClicked(View view) {
        addDoctorPhotoDialog.show();
    }

    public void onSaveButtonClicked(View view) {
        if (!Validation.validateRequired(docNameInput, required)
                | !Validation.validateEmail(docEmailInput, invalidEmailAddress)
                | !Validation.validateUrl(docWebsiteInput, invalidUrl)) {
            return;
        }

        saveDoctor();

        startActivity(new Intent(this, DoctorListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    private void saveDoctor() {
        doctor.setName(docNameInput.getText().toString());
        doctor.setPhone(docPhoneInput.getText().toString());
        doctor.setAddress(docAddressInput.getText().toString());
        doctor.setEmail(docEmailInput.getText().toString());
        doctor.setWebsite(docWebsiteInput.getText().toString());
        doctor.setNotes(notesInput.getText().toString());

        doctorController.save(doctor);
    }

    public void onCancelButtonClicked(View view) {
        finish();
    }
}
