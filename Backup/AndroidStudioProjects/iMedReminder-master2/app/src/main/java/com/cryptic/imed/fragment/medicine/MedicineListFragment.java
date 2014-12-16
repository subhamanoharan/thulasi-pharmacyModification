package com.cryptic.imed.fragment.medicine;

import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.*;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.DashboardActivity;
import com.cryptic.imed.activity.medicine.AddEditMedicineActivity;
import com.cryptic.imed.activity.medicine.MedicineDetailsActivity;
import com.cryptic.imed.common.Constants;
import com.cryptic.imed.controller.MedicineController;
import com.cryptic.imed.domain.Medicine;
import com.cryptic.imed.util.StringUtils;
import com.cryptic.imed.util.adapter.Filterable;
import com.cryptic.imed.util.adapter.FilterableArrayAdapter;
import com.cryptic.imed.util.adapter.TextWatcherAdapter;
import com.cryptic.imed.util.photo.util.ImageUtils;
import com.cryptic.imed.util.view.CompatibilityUtils;
import com.cryptic.imed.util.view.DualPaneUtils;
import com.cryptic.imed.util.view.TwoLineListItemWithImageView;
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
public class MedicineListFragment extends RoboListFragment {
    public static final String KEY_MEDICINE = "medicine";

    @Inject
    private Application application;
    @Inject
    private LayoutInflater layoutInflater;
    @Inject
    private MedicineController medicineController;

    @InjectFragment(R.id.details_container)
    @Nullable
    private MedicineDetailsFragment medicineDetailsFragment;

    @InjectResource(R.string.x_units_available)
    private String xUnitsAvailable;
    @InjectResource(R.string.edit)
    private String edit;
    @InjectResource(R.string.delete)
    private String delete;
    @InjectResource(R.drawable.ic_default_med)
    private Drawable defaultMedicinePhoto;

    private boolean dualPane;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new MedicineListAdapter());
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        EditText filterInput = (EditText) getActivity().findViewById(R.id.filter_input);
        filterInput.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                ((MedicineListAdapter) getListAdapter()).getFilter().filter(s, new Filter.FilterListener() {
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
        assert medicineDetailsFragment != null;
        medicineDetailsFragment.setMedicine(selectedItem == null ? null : (Medicine) selectedItem);
        medicineDetailsFragment.updateView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_view, container, false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Medicine selectedMedicine = (Medicine) getListAdapter().getItem(info.position);

        menu.setHeaderTitle(selectedMedicine.getName());
        menu.add(Menu.NONE, Constants.ID_CONTEXT_MENU_EDIT, 0, edit);
        menu.add(Menu.NONE, Constants.ID_CONTEXT_MENU_DELETE, 1, delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Medicine selectedMedicine = (Medicine) getListAdapter().getItem(info.position);

        switch (item.getItemId()) {
            case Constants.ID_CONTEXT_MENU_EDIT:
                Intent intent = new Intent(application, AddEditMedicineActivity.class);
                intent.putExtra(KEY_MEDICINE, selectedMedicine);
                startActivity(intent);
                return true;
            case Constants.ID_CONTEXT_MENU_DELETE:
                deleteMedicineAndUpdateView(selectedMedicine);
                return true;
        }

        return false;
    }

    public void deleteMedicineAndUpdateView(Medicine selectedMedicine) {
        deleteMedicine(selectedMedicine);
        int selectedMedicineIndex = updateMedicineList(selectedMedicine);
        if (dualPane) {
            try {
                selectItemInList(selectedMedicineIndex);
            } catch (ArrayIndexOutOfBoundsException e) {
                selectItemInList(selectedMedicineIndex - 1);
            }
        }
    }

    private void deleteMedicine(Medicine selectedMedicine) {
        selectedMedicine.setDeleted(true);
        medicineController.save(selectedMedicine);
    }

    private int updateMedicineList(Medicine selectedMedicine) {
        MedicineListAdapter medicineListAdapter = (MedicineListAdapter) getListAdapter();

        int selectedMedicineIndex = medicineListAdapter.getPosition(selectedMedicine);

        medicineListAdapter.remove(selectedMedicine);
        medicineListAdapter.notifyDataSetInvalidated();

        return selectedMedicineIndex;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ViewUtils.addNewEntityMenuItem(menu, R.string.new_medicine);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Constants.ID_OPTIONS_MENU_ADD:
                startActivity(new Intent(application, AddEditMedicineActivity.class));
                break;
            case android.R.id.home:
                startActivity(new Intent(application, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }

        return false;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Medicine selectedMedicine = (Medicine) getListAdapter().getItem(position);

        if (dualPane) {
            getListView().setItemChecked(position, true);
            updateDetailsFragment(selectedMedicine);
        } else {
            Intent intent = new Intent(application, MedicineDetailsActivity.class);
            intent.putExtra(KEY_MEDICINE, selectedMedicine);
            startActivity(intent);
        }
    }


    private class MedicineListAdapter extends FilterableArrayAdapter {
        MedicineListAdapter() {
            super(application, 0);

            addAll(medicineController.findByDeleted(false));

            sort(new Comparator<Filterable>() {
                @Override
                public int compare(Filterable lhs, Filterable rhs) {
                    return ((Medicine) lhs).getName().compareTo(((Medicine) rhs).getName());
                }
            });
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Medicine medicine = (Medicine) getItem(position);
            return TwoLineListItemWithImageView.getView(layoutInflater, convertView, parent, medicine.getName(),
                    String.format(xUnitsAvailable, StringUtils.dropDecimalIfRoundNumber(medicine.getCurrentStock()),
                            medicine.getMedicationUnit()),
                    ImageUtils.getNonEmptyImage(medicine.getPhoto(), defaultMedicinePhoto));
        }
    }
}
