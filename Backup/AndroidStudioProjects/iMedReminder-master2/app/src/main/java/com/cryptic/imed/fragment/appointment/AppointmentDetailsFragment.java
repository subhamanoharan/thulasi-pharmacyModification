package com.cryptic.imed.fragment.appointment;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ScrollView;
import android.widget.TextView;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.DashboardActivity;
import com.cryptic.imed.activity.appointment.AddEditAppointmentActivity;
import com.cryptic.imed.activity.appointment.AppointmentListActivity;
import com.cryptic.imed.activity.doctor.DoctorDetailsActivity;
import com.cryptic.imed.activity.pharmacy.PharmacyDetailsActivity;
import com.cryptic.imed.controller.AppointmentController;
import com.cryptic.imed.domain.Appointment;
import com.cryptic.imed.fragment.doctor.DoctorListFragment;
import com.cryptic.imed.fragment.pharmacy.PharmacyListFragment;
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
public class AppointmentDetailsFragment extends RoboFragment {
    @Inject
    private Application application;
    @Inject
    private AppointmentController appointmentController;

    @InjectFragment(R.id.list_container)
    @Nullable
    private AppointmentListFragment appointmentListFragment;

    @InjectView(R.id.appointment_details_view)
    private ScrollView appointmentDetailsView;
    @InjectView(R.id.title)
    private TextView titleTextView;
    @InjectView(R.id.details)
    private TextView detailsTextView;
    @InjectView(R.id.appointment_with)
    private TextView appointmentWithTextView;
    @InjectView(R.id.appointment_time)
    private TextView appointmentTimeTextView;
    @InjectView(R.id.remind_before)
    private TextView remindBeforeTextView;

    @InjectResource(R.string.not_available)
    private String notAvailable;

    private boolean dualPanel;
    private Appointment appointment;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        appointmentWithTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAppointmentWithSubjectClicked();
            }
        });

        CompatibilityUtils.setHomeButtonEnabled(true, getActivity());

        dualPanel = DualPaneUtils.isDualPane(getActivity(), R.id.list_container);
    }

    public void onAppointmentWithSubjectClicked() {
        if (appointment.getDoctor() != null) {
            Intent intent = new Intent(application, DoctorDetailsActivity.class);
            intent.putExtra(DoctorListFragment.KEY_DOCTOR, appointment.getDoctor());
            startActivity(intent);
        } else if (appointment.getPharmacy() != null) {
            Intent intent = new Intent(application, PharmacyDetailsActivity.class);
            intent.putExtra(PharmacyListFragment.KEY_PHARMACY, appointment.getPharmacy());
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_appointment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateView();
    }

    public void updateView() {
        if (appointment != null) {
            titleTextView.setText(appointment.getTitle());
            detailsTextView.setText(StringUtils.getNonEmptyString(appointment.getDetails(), notAvailable));
            appointmentTimeTextView.setText(appointment.getFormattedAppointmentTime());
            remindBeforeTextView.setText(getRemindBeforeText());

            if (appointment.getDoctor() != null) {
                appointmentController.refresh(appointment.getDoctor());
                appointmentWithTextView.setText(appointment.getDoctor().getName());
                updateAppointmentWithTextViewToIndicateClickability();
            } else if (appointment.getPharmacy() != null) {
                appointmentController.refresh(appointment.getPharmacy());
                appointmentWithTextView.setText(appointment.getPharmacy().getName());
                updateAppointmentWithTextViewToIndicateClickability();
            } else {
                appointmentWithTextView.setText(appointment.getAppointmentType().getUserFriendlyName());
                updateAppointmentWithTextViewToRemoveClickabilityIndication();
            }

            appointmentDetailsView.setVisibility(View.VISIBLE);
            setHasOptionsMenu(true);
        } else {
            appointmentDetailsView.setVisibility(View.GONE);
            setHasOptionsMenu(false);
        }
    }

    private void updateAppointmentWithTextViewToIndicateClickability() {
        appointmentWithTextView.setBackgroundResource(R.drawable.textview_border);
        appointmentWithTextView.setPadding(5, 5, 5, 5);
    }

    private void updateAppointmentWithTextViewToRemoveClickabilityIndication() {
        appointmentWithTextView.setBackgroundResource(0);
        appointmentWithTextView.setPadding(0, 0, 0, 0);
    }

    private String getRemindBeforeText() {
        return StringUtils.dropDecimalIfRoundNumber(appointment.getRemindBefore())
                + " " + appointment.getTimeIntervalUnit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_delete_options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent intent = new Intent(application, AddEditAppointmentActivity.class);
                intent.putExtra(AppointmentListFragment.KEY_APPOINTMENT, appointment);
                startActivity(intent);
                break;
            case R.id.menu_delete:
                if (dualPanel) {
                    assert appointmentListFragment != null;
                    appointmentListFragment.deleteAppointmentAndUpdateView(appointment);
                } else {
                    appointmentController.delete(appointment);

                    Intent appointmentListActivityIntent = new Intent(application, AppointmentListActivity.class);
                    startActivity(appointmentListActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().finish();
                }
                break;
            case android.R.id.home:
                startActivity(new Intent(application, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }

        return false;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
