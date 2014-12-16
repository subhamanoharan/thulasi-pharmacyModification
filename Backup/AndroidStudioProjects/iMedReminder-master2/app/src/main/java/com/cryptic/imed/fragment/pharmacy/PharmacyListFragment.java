package com.cryptic.imed.fragment.pharmacy;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.*;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.DashboardActivity;
import com.cryptic.imed.activity.pharmacy.AddEditPharmacyActivity;
import com.cryptic.imed.activity.pharmacy.PharmacyDetailsActivity;
import com.cryptic.imed.common.Constants;
import com.cryptic.imed.controller.PharmacyController;
import com.cryptic.imed.domain.Pharmacy;
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
public class PharmacyListFragment extends RoboListFragment {
    public static final String KEY_PHARMACY = "pharmacy";

    @Inject
    private Application application;
    @Inject
    private LayoutInflater layoutInflater;
    @Inject
    private PharmacyController pharmacyController;

    @InjectFragment(R.id.details_container)
    @Nullable
    private PharmacyDetailsFragment pharmacyDetailsFragment;

    @InjectResource(R.string.edit)
    private String edit;
    @InjectResource(R.string.delete)
    private String delete;

    private boolean dualPane;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new PharmacyListAdapter());
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        EditText filterInput = (EditText) getActivity().findViewById(R.id.filter_input);
        filterInput.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                ((PharmacyListAdapter) getListAdapter()).getFilter().filter(s, new Filter.FilterListener() {
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
        assert pharmacyDetailsFragment != null;
        pharmacyDetailsFragment.setPharmacy(selectedItem == null ? null : (Pharmacy) selectedItem);
        pharmacyDetailsFragment.updateView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_view, container, false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Pharmacy selectedPharmacy = (Pharmacy) getListAdapter().getItem(info.position);

        menu.setHeaderTitle(selectedPharmacy.getName());
        menu.add(Menu.NONE, Constants.ID_CONTEXT_MENU_EDIT, 0, edit);
        menu.add(Menu.NONE, Constants.ID_CONTEXT_MENU_DELETE, 1, delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Pharmacy selectedPharmacy = (Pharmacy) getListAdapter().getItem(info.position);

        switch (item.getItemId()) {
            case Constants.ID_CONTEXT_MENU_EDIT:
                Intent intent = new Intent(application, AddEditPharmacyActivity.class);
                intent.putExtra(KEY_PHARMACY, selectedPharmacy);
                startActivity(intent);
                return true;
            case Constants.ID_CONTEXT_MENU_DELETE:
                deletePharmacyAndUpdateView(selectedPharmacy);
                return true;
        }

        return false;
    }

    public void deletePharmacyAndUpdateView(Pharmacy pharmacy) {
        deletePharmacy(pharmacy);
        int selectedPharmacyIndex = updatePharmacyList(pharmacy);
        if (dualPane) {
            try {
                selectItemInList(selectedPharmacyIndex);
            } catch (ArrayIndexOutOfBoundsException e) {
                selectItemInList(selectedPharmacyIndex - 1);
            }
        }
    }

    private void deletePharmacy(Pharmacy pharmacy) {
        pharmacy.setDeleted(true);
        pharmacyController.save(pharmacy);
    }

    private int updatePharmacyList(Pharmacy selectedPharmacy) {
        PharmacyListAdapter pharmacyListAdapter = (PharmacyListAdapter) getListAdapter();

        int selectedPharmacyIndex = pharmacyListAdapter.getPosition(selectedPharmacy);

        pharmacyListAdapter.remove(selectedPharmacy);
        pharmacyListAdapter.notifyDataSetInvalidated();

        return selectedPharmacyIndex;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ViewUtils.addNewEntityMenuItem(menu, R.string.new_pharmacy);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Constants.ID_OPTIONS_MENU_ADD:
                startActivity(new Intent(application, AddEditPharmacyActivity.class));
                break;
            case android.R.id.home:
                startActivity(new Intent(application, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }

        return false;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Pharmacy selectedPharmacy = (Pharmacy) getListAdapter().getItem(position);

        if (dualPane) {
            getListView().setItemChecked(position, true);
            updateDetailsFragment(selectedPharmacy);
        } else {
            Intent intent = new Intent(application, PharmacyDetailsActivity.class);
            intent.putExtra(KEY_PHARMACY, selectedPharmacy);
            startActivity(intent);
        }
    }


    private class PharmacyListAdapter extends FilterableArrayAdapter {
        PharmacyListAdapter() {
            super(application, 0);

            addAll(pharmacyController.findByDeleted(false));

            sort(new Comparator<Filterable>() {
                @Override
                public int compare(Filterable lhs, Filterable rhs) {
                    return ((Pharmacy) lhs).getName().compareTo(((Pharmacy) rhs).getName());
                }
            });
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Pharmacy pharmacy = (Pharmacy) getItem(position);
            return TwoLineListItemView.getView(layoutInflater, convertView, parent,
                    pharmacy.getName(), pharmacy.getAddress());
        }
    }
}
