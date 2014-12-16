package com.cryptic.imed.activity.appointment;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.DashboardActivity;
import com.cryptic.imed.activity.prescription.PickDoctorActivity;
import com.cryptic.imed.common.Constants;
import com.cryptic.imed.controller.AppointmentController;
import com.cryptic.imed.domain.Appointment;
import com.cryptic.imed.domain.Doctor;
import com.cryptic.imed.domain.Pharmacy;
import com.cryptic.imed.domain.enums.AppointmentType;
import com.cryptic.imed.domain.enums.TimeIntervalUnit;
import com.cryptic.imed.fragment.appointment.AppointmentListFragment;
import com.cryptic.imed.util.StringUtils;
import com.cryptic.imed.util.Validation;
import com.cryptic.imed.util.view.CompatibilityUtils;
import com.cryptic.imed.util.view.DateTimePickerDialog;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author sharafat
 */
@ContentView(R.layout.new_appointment)
public class AddEditAppointmentActivity extends RoboActivity {
    private static final int REQ_CODE_PICK_DOCTOR = 1;
    private static final int REQ_CODE_PICK_PHARMACY = 2;

    @Inject
    private Application application;
    @Inject
    private AppointmentController appointmentController;

    @InjectView(R.id.appointment_title_input)
    private EditText titleInput;
    @InjectView(R.id.details_input)
    private EditText detailsInput;
    @InjectView(R.id.appointment_with_input)
    private Spinner appointmentWithInput;
    @InjectView(R.id.pick_doctor_pharmacy_view)
    private TextView pickSubjectView;
    @InjectView(R.id.start_date_btn)
    private Button startDateBtn;
    @InjectView(R.id.reminder_offset_input)
    private EditText reminderOffsetInput;
    @InjectView(R.id.reminder_interval_type_input)
    private Spinner reminderIntervalTypeInput;

    @InjectResource(R.string.required)
    private String required;
    @InjectResource(R.string.tap_to_select_x)
    private String tapToSelect;

    private DateTimePickerDialog dateTimePickerDialog;
    private Appointment appointment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareAppointmentWithInput();
        prepareReminderIntervalTypeAdapter();
        prepareAppointment(getIntent().getSerializableExtra(AppointmentListFragment.KEY_APPOINTMENT));
        prepareDateTimePickerDialog();
        startDateBtn.setText(DateFormat.format(Constants.GENERAL_DATE_TIME_FORMAT, appointment.getTime()));

        CompatibilityUtils.setHomeButtonEnabled(true, this);
    }

    private void prepareAppointment(Serializable appointmentToBeEdited) {
        if (appointmentToBeEdited == null) {
            appointment = new Appointment();
            appointment.setAppointmentType(AppointmentType.DOCTOR);
            appointment.setTime(new Date());
        } else {
            appointment = (Appointment) appointmentToBeEdited;
            updateViewWithAppointmentDetails();
        }
    }

    @SuppressWarnings("unchecked")
    private void updateViewWithAppointmentDetails() {
        titleInput.setText(appointment.getTitle());
        detailsInput.setText(appointment.getDetails());
        appointmentWithInput.setSelection(((ArrayAdapter<AppointmentType>) appointmentWithInput.getAdapter())
                .getPosition(appointment.getAppointmentType()));
        startDateBtn.setText(appointment.getFormattedAppointmentTime());
        reminderOffsetInput.setText(StringUtils.dropDecimalIfRoundNumber(appointment.getRemindBefore()));
        reminderIntervalTypeInput.setSelection(((ArrayAdapter<TimeIntervalUnit>) reminderIntervalTypeInput.getAdapter())
                .getPosition(appointment.getTimeIntervalUnit()));

        if (appointment.getDoctor() != null) {
            appointmentController.refresh(appointment.getDoctor());
            pickSubjectView.setText(appointment.getDoctor().getName());
        } else if (appointment.getPharmacy() != null) {
            appointmentController.refresh(appointment.getPharmacy());
            pickSubjectView.setText(appointment.getPharmacy().getName());
        } else {
            pickSubjectView.setText(String.format(tapToSelect, appointment.getAppointmentType()));
        }

        pickSubjectView.setVisibility(appointment.getAppointmentType() == AppointmentType.OTHER
                ? View.GONE : View.VISIBLE);
    }

    private void prepareAppointmentWithInput() {
        final ArrayAdapter<AppointmentType> appointmentWithAdapter = new ArrayAdapter<AppointmentType>(
                this, android.R.layout.simple_spinner_item, AppointmentType.values());
        appointmentWithAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appointmentWithInput.setAdapter(appointmentWithAdapter);
        appointmentWithInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppointmentType selectedAppointmentType = appointmentWithAdapter.getItem(position);

                if (selectedAppointmentType != AppointmentType.OTHER) {
                    pickSubjectView.setText(String.format(tapToSelect, selectedAppointmentType));
                    pickSubjectView.setVisibility(View.VISIBLE);
                } else {
                    pickSubjectView.setVisibility(View.GONE);
                }

                appointment.setDoctor(null);
                appointment.setPharmacy(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //ignore
            }
        });
    }

    private void prepareReminderIntervalTypeAdapter() {
        ArrayAdapter<TimeIntervalUnit> reminderIntervalTypeAdapter = new ArrayAdapter<TimeIntervalUnit>(
                this, android.R.layout.simple_spinner_item, TimeIntervalUnit.values());
        reminderIntervalTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderIntervalTypeInput.setAdapter(reminderIntervalTypeAdapter);
    }

    private void prepareDateTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(appointment.getTime());
        dateTimePickerDialog = new DateTimePickerDialog(this, new DateTimePickerDialog.OnDateTimeSetListener() {
            @Override
            public void onDateTimeSet(DatePicker datePickerView, TimePicker timePickerView,
                                      int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
                Date selectedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth, hourOfDay, minute).getTime();
                appointment.setTime(selectedDate);
                startDateBtn.setText(DateFormat.format(Constants.GENERAL_DATE_TIME_FORMAT, selectedDate));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
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

    public void onStartDateButtonClicked(View view) {
        dateTimePickerDialog.show();
    }

    public void onSelectAppointmentWithSubjectClicked(View view) {
        Class clazz = null;
        int pickRequest = 0;

        AppointmentType selectedAppointmentType = (AppointmentType) appointmentWithInput.getSelectedItem();
        if (selectedAppointmentType == AppointmentType.DOCTOR) {
            clazz = PickDoctorActivity.class;
            pickRequest = REQ_CODE_PICK_DOCTOR;
        } else if (selectedAppointmentType == AppointmentType.PHARMACY) {
            clazz = PickPharmacyActivity.class;
            pickRequest = REQ_CODE_PICK_PHARMACY;
        }

        startActivityForResult(new Intent(application, clazz), pickRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_PICK_DOCTOR:
                    Doctor selectedDoctor = (Doctor) data.getSerializableExtra(PickDoctorActivity.KEY_SELECTED_DOCTOR);
                    appointment.setDoctor(selectedDoctor);
                    pickSubjectView.setText(selectedDoctor.getName());
                    break;
                case REQ_CODE_PICK_PHARMACY:
                    Pharmacy selectedPharmacy = (Pharmacy)
                            data.getSerializableExtra(PickPharmacyActivity.KEY_SELECTED_PHARMACY);
                    appointment.setPharmacy(selectedPharmacy);
                    pickSubjectView.setText(selectedPharmacy.getName());
                    break;
            }
        }
    }

    public void onSaveButtonClicked(View view) {
        if (!Validation.validateRequired(titleInput, required)) {
            return;
        }

        saveAppointment();

        startActivity(new Intent(this, AppointmentListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    private void saveAppointment() {
        appointment.setTitle(titleInput.getText().toString());
        appointment.setDetails(detailsInput.getText().toString());
        appointment.setAppointmentType((AppointmentType) appointmentWithInput.getSelectedItem());
        appointment.setRemindBefore(StringUtils.parseFloat(reminderOffsetInput));
        appointment.setTimeIntervalUnit((TimeIntervalUnit) reminderIntervalTypeInput.getSelectedItem());

        appointmentController.save(appointment);
    }

    public void onCancelButtonClicked(View view) {
        finish();
    }
}
