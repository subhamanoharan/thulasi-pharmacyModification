package com.cryptic.imed.activity.prescription;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cryptic.imed.R;
import com.cryptic.imed.common.Constants;
import com.cryptic.imed.domain.Dosage;
import com.cryptic.imed.domain.Medicine;
import com.cryptic.imed.domain.PrescriptionMedicine;
import com.cryptic.imed.util.StringUtils;
import com.cryptic.imed.util.view.ViewUtils;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.*;


/**
 * @author sharafat
 */
@ContentView(R.layout.medicine_schedule)
public class MedicineScheduleActivity extends RoboActivity {
    public static final String KEY_DOSAGE_DETAILS = "dosage_details";

    private static final int DEFAULT_DOSAGE_SCHEDULE_TIME_HOUR = 6;

    @Inject
    private Application application;
    @Inject
    private LayoutInflater layoutInflater;

    @InjectView(R.id.med_name)
    private TextView medNameTextView;
    @InjectView(R.id.start_date_btn)
    private Button startDateBtn;
    @InjectView(R.id.no_of_doses_input)
    private EditText noOfDosesInput;
    @InjectView(R.id.day_interval_input)
    private EditText dayIntervalInput;
    @InjectView(R.id.no_of_days_input)
    private EditText noOfDaysInput;
    @InjectView(android.R.id.list)
    private ListView listView;
    @InjectView(R.id.add_reminder_btn)
    private Button addReminderBtn;

    @InjectResource(R.string.dose_no)
    private String dose;
    @InjectResource(R.string.units_at)
    private String unitsAt;
    @InjectResource(R.string.delete)
    private String delete;

    private Medicine medicine;
    private PrescriptionMedicine prescriptionMedicine;
    private DatePickerDialog datePickerDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        medicine = (Medicine) getIntent().getSerializableExtra(AddMedicineActivity.KEY_SELECTED_MEDICINE);

        medNameTextView.setText(medicine.getName());

        Calendar calendar = Calendar.getInstance();
        Date todaysDate = calendar.getTime();

        prescriptionMedicine = new PrescriptionMedicine();
        prescriptionMedicine.setMedicine(medicine);
        prescriptionMedicine.setDosageReminders(new ArrayList<Dosage>());
        prescriptionMedicine.setStartDate(todaysDate);

        startDateBtn.setText(DateFormat.format(Constants.GENERAL_DATE_FORMAT, todaysDate));
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date selectedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                prescriptionMedicine.setStartDate(selectedDate);
                startDateBtn.setText(DateFormat.format(Constants.GENERAL_DATE_FORMAT, selectedDate));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        listView.setAdapter(new DosageListAdapter());
    }

    public void onStartDateButtonClicked(View view) {
        datePickerDialog.show();
    }

    public void onAddReminderTimesButtonClicked(View view) {
        /*
        After adding, the list items will be laid-out again and in that case the edittext values will be
        misplaced when Android tries to preserve their values. Therefore, we need to save the values of the
        edittexts and after addition when the list items are laid-out again, the edittext values are set
        from these stored values.
        */
        storeDosageQuantity();

        prescriptionMedicine.getDosageReminders().add(createDosage());
        ((DosageListAdapter) listView.getAdapter()).notifyDataSetChanged();
        ViewUtils.setListViewHeightBasedOnChildren(listView);
    }

    private Dosage createDosage() {
        Dosage dosage = new Dosage();

        dosage.setDoseNo(listView.getAdapter().getCount() + 1);
        dosage.setPrescriptionMedicine(prescriptionMedicine);
        dosage.setTime(new GregorianCalendar(0, 0, 0, DEFAULT_DOSAGE_SCHEDULE_TIME_HOUR, 0).getTime());

        return dosage;
    }

    public void onSaveButtonClicked(View view) {
        storeUserInputs();
        setResult(RESULT_OK, getIntent().putExtra(KEY_DOSAGE_DETAILS, prescriptionMedicine));
        finish();
    }

    private void storeUserInputs() {
        prescriptionMedicine.setDosesToTake(StringUtils.parseInt(noOfDosesInput));
        prescriptionMedicine.setDayInterval(StringUtils.parseInt(dayIntervalInput));
        prescriptionMedicine.setTotalDaysToTake(StringUtils.parseInt(noOfDaysInput));
        storeDosageQuantity();
    }

    private void storeDosageQuantity() {
        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            EditText quantityInput = (EditText) listView.getChildAt(i).findViewById(R.id.quantity_input);
            ((Dosage) listView.getAdapter().getItem(i)).setQuantity(StringUtils.parseFloat(quantityInput));
        }
    }

    public void onCancelButtonClicked(View view) {
        finish();
    }

    private void deleteDosageAndUpdateList(Dosage dosage) {
        /*
        After deleting, the list items will be laid-out again and in that case the edittext values will be
        misplaced when Android tries to preserve their values. Therefore, we need to save the values of the
        edittexts and after deleting when the list items are laid-out again, the edittext values are set
        from these stored values.
        */
        storeDosageQuantity();

        DosageListAdapter dosageListAdapter = (DosageListAdapter) listView.getAdapter();
        dosageListAdapter.remove(dosage);
        dosageListAdapter.notifyDataSetInvalidated();
        ViewUtils.setListViewHeightBasedOnChildren(listView);

        renumerateDosages(dosageListAdapter);
    }

    private void renumerateDosages(DosageListAdapter dosageListAdapter) {
        for (int i = 0; i < dosageListAdapter.getCount(); i++) {
            dosageListAdapter.getItem(i).setDoseNo(i + 1);
        }
    }


    private class DosageListAdapter extends ArrayAdapter<Dosage> {
        DosageListAdapter() {
            super(application, 0, (List<Dosage>) prescriptionMedicine.getDosageReminders());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_item_dosage, parent, false);
            }

            final TextView doseNoTextView = (TextView) convertView.findViewById(R.id.dose_no);
            final EditText quantityEditText = (EditText) convertView.findViewById(R.id.quantity_input);
            final TextView unitsTextView = (TextView) convertView.findViewById(R.id.units_text);
            final Button dosageTimeBtn = (Button) convertView.findViewById(R.id.time_btn);
            final ImageView deleteBtn = (ImageView) convertView.findViewById(R.id.delete_btn);

            final Dosage dosage = getItem(position);

            doseNoTextView.setText(String.format(dose, dosage.getDoseNo()));
            quantityEditText.setText(StringUtils.dropDecimalIfRoundNumber(dosage.getQuantity()));
            unitsTextView.setText(String.format(unitsAt, medicine.getMedicationUnit()));

            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(dosage.getTime());
            dosageTimeBtn.setText(DateFormat.format(Constants.GENERAL_TIME_FORMAT, dosage.getTime()));
            dosageTimeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new TimePickerDialog(MedicineScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            calendar.set(Calendar.HOUR, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);

                            dosage.setTime(calendar.getTime());

                            dosageTimeBtn.setText(DateFormat.format(Constants.GENERAL_TIME_FORMAT, calendar.getTime()));
                        }
                    }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false).show();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDosageAndUpdateList(dosage);
                }
            });

            return convertView;
        }
    }
}
