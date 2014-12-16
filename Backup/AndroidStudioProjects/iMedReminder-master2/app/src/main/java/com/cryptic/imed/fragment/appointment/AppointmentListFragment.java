package com.cryptic.imed.fragment.appointment;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateFormat;
import android.view.*;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.DashboardActivity;
import com.cryptic.imed.activity.appointment.AddEditAppointmentActivity;
import com.cryptic.imed.activity.appointment.AppointmentDetailsActivity;
import com.cryptic.imed.common.Constants;
import com.cryptic.imed.controller.AppointmentController;
import com.cryptic.imed.domain.Appointment;
import com.cryptic.imed.util.adapter.Filterable;
import com.cryptic.imed.util.adapter.FilterableArrayAdapter;
import com.cryptic.imed.util.adapter.TextWatcherAdapter;
import com.cryptic.imed.util.view.CompatibilityUtils;
import com.cryptic.imed.util.view.DualPaneUtils;
import com.cryptic.imed.util.view.TwoLineListItemView;
import com.cryptic.imed.util.view.ViewUtils;
import com.google.inject.Inject;
import roboguice.fragment.RoboListFragment;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectResource;

import javax.annotation.Nullable;
import java.util.Comparator;

/**
 * @author sharafat
 */
public class AppointmentListFragment extends RoboListFragment {
    public static final String KEY_APPOINTMENT = "appointment";

    @Inject
    private Application application;
    @Inject
    private LayoutInflater layoutInflater;
    @Inject
    private AppointmentController appointmentController;

    @InjectFragment(R.id.details_container)
    @Nullable
    private AppointmentDetailsFragment appointmentDetailsFragment;

    @InjectResource(R.string.edit)
    private String edit;
    @InjectResource(R.string.delete)
    private String delete;

    private boolean dualPane;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new AppointmentListAdapter());
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        EditText filterInput = (EditText) getActivity().findViewById(R.id.filter_input);
        filterInput.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                ((AppointmentListAdapter) getListAdapter()).getFilter().filter(s, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        if (dualPane) {
                            selectFirstItemInList();
                        }
                    }
                });
            }
        });

        registerForContextMenu(getListView());
        setHasOptionsMenu(true);
        CompatibilityUtils.setHomeButtonEnabled(true, getActivity());

        dualPane = DualPaneUtils.isDualPane(getActivity(), R.id.details_container);
        if (dualPane) {
            selectFirstItemInList();
        }
    }

    private void selectFirstItemInList() {
        selectItemInList(0);
    }

    private void selectItemInList(int position) {
        int itemCount = getListAdapter().getCount();
        if (itemCount > 0) {
            if (position < itemCount) {
                getListView().setItemChecked(position, true);
                updateDetailsFragment(getListAdapter().getItem(position));
            } else {
                throw new ArrayIndexOutOfBoundsException("list item count = " + itemCount
                        + ", but position given = " + position);
            }
        } else {
            updateDetailsFragment(null);
        }
    }

    private void updateDetailsFragment(Object selectedItem) {
        assert appointmentDetailsFragment != null;
        appointmentDetailsFragment.setAppointment(selectedItem == null ? null : (Appointment) selectedItem);
        appointmentDetailsFragment.updateView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_view, container, false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Appointment selectedAppointment = (Appointment) getListAdapter().getItem(info.position);

        menu.setHeaderTitle(selectedAppointment.getTitle());
        menu.add(Menu.NONE, Constants.ID_CONTEXT_MENU_EDIT, 0, edit);
        menu.add(Menu.NONE, Constants.ID_CONTEXT_MENU_DELETE, 1, delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Appointment selectedAppointment = (Appointment) getListAdapter().getItem(info.position);

        switch (item.getItemId()) {
            case Constants.ID_CONTEXT_MENU_EDIT:
                Intent intent = new Intent(application, AddEditAppointmentActivity.class);
                intent.putExtra(KEY_APPOINTMENT, selectedAppointment);
                startActivity(intent);
                return true;
            case Constants.ID_CONTEXT_MENU_DELETE:
                deleteAppointmentAndUpdateView(selectedAppointment);
                return true;
        }

        return false;
    }

    public void deleteAppointmentAndUpdateView(Appointment appointment) {
        appointmentController.delete(appointment);
        int selectedAppointmentIndex = updateAppointmentList(appointment);
        if (dualPane) {
            try {
                selectItemInList(selectedAppointmentIndex);
            } catch (ArrayIndexOutOfBoundsException e) {
                selectItemInList(selectedAppointmentIndex - 1);
            }
        }
    }

    private int updateAppointmentList(Appointment selectedAppointment) {
        AppointmentListAdapter appointmentListAdapter = (AppointmentListAdapter) getListAdapter();

        int selectedAppointmentIndex = appointmentListAdapter.getPosition(selectedAppointment);

        appointmentListAdapter.remove(selectedAppointment);
        appointmentListAdapter.notifyDataSetInvalidated();

        return selectedAppointmentIndex;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ViewUtils.addNewEntityMenuItem(menu, R.string.new_appointment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Constants.ID_OPTIONS_MENU_ADD:
                startActivity(new Intent(application, AddEditAppointmentActivity.class));
                break;
            case android.R.id.home:
                startActivity(new Intent(application, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }

        return false;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Appointment selectedAppointment = (Appointment) getListAdapter().getItem(position);

        if (dualPane) {
            getListView().setItemChecked(position, true);
            updateDetailsFragment(selectedAppointment);
        } else {
            Intent intent = new Intent(application, AppointmentDetailsActivity.class);
            intent.putExtra(KEY_APPOINTMENT, selectedAppointment);
            startActivity(intent);
        }
    }


    private class AppointmentListAdapter extends FilterableArrayAdapter {
        AppointmentListAdapter() {
            super(application, 0);

            addAll(appointmentController.list());

            sort(new Comparator<Filterable>() {
                @Override
                public int compare(Filterable lhs, Filterable rhs) {
                    return ((Appointment) lhs).getTitle().compareTo(((Appointment) rhs).getTitle());
                }
            });
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Appointment appointment = (Appointment) getItem(position);
            return TwoLineListItemView.getView(layoutInflater, convertView, parent,
                    appointment.getTitle(),
                    DateFormat.format(Constants.GENERAL_DATE_TIME_FORMAT, appointment.getTime()).toString());
        }
    }
}
