package com.cryptic.imed.activity.prescription;

import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.*;
import android.widget.*;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.DashboardActivity;
import com.cryptic.imed.common.Constants;
import com.cryptic.imed.controller.PrescriptionController;
import com.cryptic.imed.domain.Doctor;
import com.cryptic.imed.domain.Prescription;
import com.cryptic.imed.domain.PrescriptionMedicine;
import com.cryptic.imed.fragment.prescription.PrescriptionListFragment;
import com.cryptic.imed.util.Validation;
import com.cryptic.imed.util.photo.util.ImageUtils;
import com.cryptic.imed.util.view.CompatibilityUtils;
import com.cryptic.imed.util.view.ViewUtils;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sharafat
 */
@ContentView(R.layout.new_prescription)
public class AddEditPrescriptionActivity extends RoboActivity {
    private static final int REQ_CODE_PICK_DOCTOR = 1;
    private static final int REQ_CODE_ADD_MEDICINE = 2;

    @Inject
    private Application application;
    @Inject
    private LayoutInflater layoutInflater;
    @Inject
    private PrescriptionController prescriptionController;

    @InjectView(R.id.title_input)
    private EditText titleInput;
    @InjectView(R.id.details_input)
    private EditText detailsInput;
    @InjectView(R.id.pick_doctor_view)
    private TextView pickDoctorView;
    @InjectView(R.id.add_medicine_btn)
    private Button addMedicineBtn;
    @InjectView(R.id.medicine_list)
    private ListView medicineListView;

    @InjectResource(R.string.remove)
    private String remove;
    @InjectResource(R.string.required)
    private String required;
    @InjectResource(R.string.tap_to_select_doctor)
    private String tapToSelectDoctor;
    @InjectResource(R.string.medicine_list_item_details)
    private String medicineListItemDetails;
    @InjectResource(R.string.everyday)
    private String everyday;
    @InjectResource(R.string.every_x_days)
    private String everyXDays;
    @InjectResource(R.drawable.ic_default_med)
    private Drawable defaultMedicinePhoto;

    private Prescription prescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preparePrescription(getIntent().getSerializableExtra(PrescriptionListFragment.KEY_PRESCRIPTION));
        registerForContextMenu(pickDoctorView);

        medicineListView.setAdapter(new PrescriptionMedicineListAdapter());

        CompatibilityUtils.setHomeButtonEnabled(true, this);
    }

    private void preparePrescription(Serializable prescriptionToBeEdited) {
        if (prescriptionToBeEdited == null) {
            prescription = new Prescription();
            prescription.setIssueDate(new Date());
            prescription.setMedicines(new ArrayList<PrescriptionMedicine>());
        } else {
            prescription = (Prescription) prescriptionToBeEdited;
            updateViewWithPrescriptionDetails();
        }
    }

    private void updateViewWithPrescriptionDetails() {
        prescriptionController.refresh(prescription);
        prescriptionController.refresh(prescription.getPrescribedBy());

        titleInput.setText(prescription.getTitle());
        detailsInput.setText(prescription.getDetails());
        pickDoctorView.setText(prescription.getPrescribedBy() == null ? tapToSelectDoctor
                : prescription.getPrescribedBy().getName());

        prescription.setMedicines(new ArrayList<PrescriptionMedicine>(prescription.getMedicines()));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v == pickDoctorView) {
            menu.add(Menu.NONE, Constants.ID_CONTEXT_MENU_DELETE, Menu.NONE, remove);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == Constants.ID_CONTEXT_MENU_DELETE) {
            prescription.setPrescribedBy(null);
            pickDoctorView.setText(tapToSelectDoctor);
            return true;
        }

        return false;
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

    public void onSelectDoctorClicked(View view) {
        startActivityForResult(new Intent(this, PickDoctorActivity.class), REQ_CODE_PICK_DOCTOR);
    }

    public void onAddMedicineButtonClicked(View view) {
        startActivityForResult(new Intent(this, AddMedicineActivity.class), REQ_CODE_ADD_MEDICINE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_PICK_DOCTOR:
                    Doctor selectedDoctor = (Doctor) data.getSerializableExtra(PickDoctorActivity.KEY_SELECTED_DOCTOR);
                    prescription.setPrescribedBy(selectedDoctor);
                    pickDoctorView.setText(selectedDoctor.getName());
                    break;
                case REQ_CODE_ADD_MEDICINE:
                    PrescriptionMedicine prescriptionMedicine = (PrescriptionMedicine)
                            data.getSerializableExtra(MedicineScheduleActivity.KEY_DOSAGE_DETAILS);
                    prescriptionMedicine.setPrescription(prescription);
                    prescription.getMedicines().add(prescriptionMedicine);

                    ((PrescriptionMedicineListAdapter) medicineListView.getAdapter()).notifyDataSetChanged();
                    ViewUtils.setListViewHeightBasedOnChildren(medicineListView);
                    break;
            }
        }
    }

    public void onSaveButtonClicked(View view) {
        if (!Validation.validateRequired(titleInput, required)) {
            return;
        }

        savePrescription();

        startActivity(new Intent(this, PrescriptionListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    private void savePrescription() {
        prescription.setTitle(titleInput.getText().toString());
        prescription.setDetails(detailsInput.getText().toString());

        prescriptionController.save(prescription);
    }

    public void onCancelButtonClicked(View view) {
        finish();
    }

    private void removePrescriptionMedicineAndUpdateView(PrescriptionMedicine prescriptionMedicine) {
        prescriptionController.delete(prescriptionMedicine);
        prescription.getMedicines().remove(prescriptionMedicine);
        ((PrescriptionMedicineListAdapter) medicineListView.getAdapter()).notifyDataSetInvalidated();
        ViewUtils.setListViewHeightBasedOnChildren(medicineListView);
    }


    private class PrescriptionMedicineListAdapter extends ArrayAdapter<PrescriptionMedicine> {
        PrescriptionMedicineListAdapter() {
            super(application, 0, (List<PrescriptionMedicine>) prescription.getMedicines());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_item_prescription_medicine, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            TextView primaryText = (TextView) convertView.findViewById(R.id.primaryText);
            TextView secondaryText = (TextView) convertView.findViewById(R.id.secondaryText);
            ImageView deleteBtn = (ImageView) convertView.findViewById(R.id.delete_btn);

            final PrescriptionMedicine prescriptionMedicine = getItem(position);

            byte[] medicinePhoto = prescriptionMedicine.getMedicine().getPhoto();
            imageView.setImageBitmap(ImageUtils.getNonEmptyImage(medicinePhoto, defaultMedicinePhoto));

            primaryText.setText(prescriptionMedicine.getMedicine().getName());

            secondaryText.setText(String.format(medicineListItemDetails,
                    prescriptionMedicine.getTotalDaysToTake(),
                    prescriptionMedicine.getDosesToTake(),
                    prescriptionMedicine.getDayInterval() == 0
                            ? everyday
                            : String.format(everyXDays, prescriptionMedicine.getDayInterval()),
                    DateFormat.format(Constants.GENERAL_DATE_FORMAT, prescriptionMedicine.getStartDate())));

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removePrescriptionMedicineAndUpdateView(prescriptionMedicine);
                }
            });

            return convertView;
        }
    }
}
