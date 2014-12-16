package com.cryptic.imed.activity.medicine;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.DashboardActivity;
import com.cryptic.imed.common.Constants;
import com.cryptic.imed.controller.MedicineController;
import com.cryptic.imed.domain.Medicine;
import com.cryptic.imed.domain.enums.MedicationUnit;
import com.cryptic.imed.domain.enums.MedicineType;
import com.cryptic.imed.fragment.medicine.MedicineListFragment;
import com.cryptic.imed.util.StringUtils;
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
@ContentView(R.layout.new_medicine)
public class AddEditMedicineActivity extends RoboActivity {
    @Inject
    private Application application;
    @Inject
    private LayoutInflater layoutInflater;
    @Inject
    private MedicineController medicineController;
    @Inject
    private PhotoTaker photoTaker;

    @InjectView(R.id.med_name_input)
    private EditText medNameInput;
    @InjectView(R.id.take_photo_btn)
    private ImageButton takePhotoButton;
    @InjectView(R.id.details_input)
    private EditText detailsInput;
    @InjectView(R.id.current_stock_input)
    private EditText currentStockInput;
    @InjectView(R.id.medication_unit_spinner)
    private Spinner medicationUnitSpinner;

    @InjectResource(R.array.medicine_photo_taking_options)
    private String[] photoTakingOptions;
    @InjectResource(R.string.add_medicine_photo)
    private String addMedicinePhoto;
    @InjectResource(R.string.select_med_photo)
    private String selectMedicinePhoto;
    @InjectResource(R.string.remove_photo)
    private String removePhoto;
    @InjectResource(R.string.required)
    private String required;

    private AlertDialog addMedicinePhotoDialog;
    private Dialog pickMedicinePhotoFromStockDialog;
    private OnPhotoTakeListener onPhotoTakeListener;

    private Medicine medicine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMedicationUnitSpinnerAdapter();
        setPhotoTakerOptions();
        createAddMedicinePhotoDialog();
        createPickMedicinePhotoFromStockDialog();
        setOnPhotoTakeListener();
        prepareMedicine(getIntent().getSerializableExtra(MedicineListFragment.KEY_MEDICINE));
        registerForContextMenu(takePhotoButton);

        CompatibilityUtils.setHomeButtonEnabled(true, this);
    }

    private void setMedicationUnitSpinnerAdapter() {
        ArrayAdapter<MedicationUnit> medicationUnitSpinnerAdapter =
                new ArrayAdapter<MedicationUnit>(this, android.R.layout.simple_spinner_item, MedicationUnit.values());
        medicationUnitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicationUnitSpinner.setAdapter(medicationUnitSpinnerAdapter);
    }

    private void setPhotoTakerOptions() {
        photoTaker.setAspectXForCropping(1);
        photoTaker.setAspectYForCropping(1);
        photoTaker.setImageWidthAfterCropping(Constants.PHOTO_SIZE);
        photoTaker.setImageHeightAfterCropping(Constants.PHOTO_SIZE);
    }

    private void createAddMedicinePhotoDialog() {
        addMedicinePhotoDialog = new AlertDialog.Builder(this).setTitle(addMedicinePhoto)
                .setItems(photoTakingOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedOptionIndex) {
                        switch (selectedOptionIndex) {
                            case 0: //take from camera
                                try {
                                    photoTaker.takePhotoFromCamera();
                                } catch (CameraUnavailableException e) {
                                    new AlertDialog.Builder(AddEditMedicineActivity.this)
                                            .setMessage(R.string.camera_unavailable).create().show();
                                    takePhotoButton.setEnabled(false);
                                }
                                break;
                            case 1: //pick from gallery
                                photoTaker.pickImageFromGallery();
                                break;
                            case 2: //pick from stock
                                showPickPhotoFromStockDialog();
                                break;
                        }
                    }
                }).create();
    }

    private void createPickMedicinePhotoFromStockDialog() {
        ListView medPhotoFromStockListView = new ListView(this);
        medPhotoFromStockListView.setBackgroundColor(Color.WHITE);
        medPhotoFromStockListView.setAdapter(new MedicineTypeListAdapter());
        medPhotoFromStockListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MedicineType medicineType = ((MedicineTypeListAdapter) parent.getAdapter()).getItem(position);

                medicine.setPhoto(ImageUtils.getByteArray(medicineType.getIcon()));
                takePhotoButton.setImageDrawable(medicineType.getIcon());

                pickMedicinePhotoFromStockDialog.hide();
            }
        });

        pickMedicinePhotoFromStockDialog = new Dialog(this);
        pickMedicinePhotoFromStockDialog.setTitle(selectMedicinePhoto);
        pickMedicinePhotoFromStockDialog.setContentView(medPhotoFromStockListView);
    }

    private void showPickPhotoFromStockDialog() {
        pickMedicinePhotoFromStockDialog.show();
    }

    private void setOnPhotoTakeListener() {
        onPhotoTakeListener = new OnPhotoTakeListener() {
            @Override
            public void onPhotoTaken(Bitmap photo) {
                if (photo != null) {
                    medicine.setPhoto(ImageUtils.getByteArray(photo));
                    takePhotoButton.setImageBitmap(photo);
                }
            }
        };
    }

    private void prepareMedicine(Serializable medicineToBeEdited) {
        if (medicineToBeEdited == null) {
            medicine = new Medicine();
        } else {
            medicine = (Medicine) medicineToBeEdited;
            updateViewWithMedicineDetails();
        }
    }

    private void updateViewWithMedicineDetails() {
        medNameInput.setText(medicine.getName());
        detailsInput.setText(medicine.getDetails());
        currentStockInput.setText(StringUtils.dropDecimalIfRoundNumber(medicine.getCurrentStock()));
        for (int i = 0; i < medicationUnitSpinner.getAdapter().getCount(); i++) {
            if (medicationUnitSpinner.getAdapter().getItem(i) == medicine.getMedicationUnit()) {
                medicationUnitSpinner.setSelection(i);
                break;
            }
        }
        if (medicine.getPhoto() != null) {
            takePhotoButton.setImageBitmap(ImageUtils.getBitmap(medicine.getPhoto()));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v == takePhotoButton && medicine.getPhoto() != null) {
            menu.add(removePhoto);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(removePhoto)) {
            medicine.setPhoto(null);
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
        addMedicinePhotoDialog.show();
    }

    public void onSaveButtonClicked(View view) {
        if (!Validation.validateRequired(medNameInput, required)) {
            return;
        }

        saveMedicine();

        startActivity(new Intent(this, MedicineListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    private void saveMedicine() {
        medicine.setName(medNameInput.getText().toString());
        medicine.setDetails(detailsInput.getText().toString());
        medicine.setCurrentStock(getCurrentStockFromUserInput());
        medicine.setMedicationUnit((MedicationUnit) medicationUnitSpinner.getSelectedItem());

        medicineController.save(medicine);
    }

    private float getCurrentStockFromUserInput() {
        String currentStock = currentStockInput.getText().toString();
        if ("".equals(currentStock)) {  //user hasn't provided any input
            return 0;
        } else {
            return Float.parseFloat(currentStock);
        }
    }

    public void onCancelButtonClicked(View view) {
        finish();
    }


    private class MedicineTypeListAdapter extends ArrayAdapter<MedicineType> {
        public MedicineTypeListAdapter() {
            super(application, 0, MedicineType.values());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                /*
                Using android.R.layout.simple_list_item_1 somehow sets its layout width as wrap_content when the list
                is placed inside a dialog.
                Interestingly, wrapping the TextView inside a RelativeLayout causes the list item to match its parent's
                width (LinearLayout doesn't work either). Therefore, a custom layout has been used as list item.
                c.f. https://groups.google.com/forum/?fromgroups#!topic/android-developers/Ylgekx-A2Xw
                 */
                convertView = layoutInflater.inflate(R.layout.list_item_pick_med_photo_from_stock, parent, false);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.text);

            MedicineType medicineType = getItem(position);

            textView.setText(medicineType.getUserFriendlyName());
            textView.setCompoundDrawablesWithIntrinsicBounds(medicineType.getIcon(), null, null, null);

            return convertView;
        }
    }
}
