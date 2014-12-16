package com.cryptic.imed.fragment.medicine;

import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.cryptic.imed.R;
import com.cryptic.imed.activity.DashboardActivity;
import com.cryptic.imed.activity.medicine.AddEditMedicineActivity;
import com.cryptic.imed.activity.medicine.MedicineListActivity;
import com.cryptic.imed.controller.MedicineController;
import com.cryptic.imed.domain.Medicine;
import com.cryptic.imed.util.StringUtils;
import com.cryptic.imed.util.photo.util.ImageUtils;
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
public class MedicineDetailsFragment extends RoboFragment {
    @Inject
    private Application application;
    @Inject
    private MedicineController medicineController;

    @InjectFragment(R.id.list_container)
    @Nullable
    private MedicineListFragment medicineListFragment;

    @InjectView(R.id.medicine_details_view)
    private ScrollView medicineDetailsView;
    @InjectView(R.id.med_name)
    private TextView medNameTextView;
    @InjectView(R.id.med_details)
    private TextView medDetailsTextView;
    @InjectView(R.id.current_stock)
    private TextView currentStockTextView;
    @InjectView(R.id.med_photo)
    private ImageView medPhotoImageView;

    @InjectResource(R.string.x_units_available)
    private String xUnitsAvailable;
    @InjectResource(R.string.not_available)
    private String notAvailable;
    @InjectResource(R.drawable.ic_default_med)
    private Drawable defaultMedicinePhoto;

    private boolean dualPanel;
    private Medicine medicine;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CompatibilityUtils.setHomeButtonEnabled(true, getActivity());

        dualPanel = DualPaneUtils.isDualPane(getActivity(), R.id.list_container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_medicine, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateView();
    }

    public void updateView() {
        if (medicine != null) {
            medNameTextView.setText(medicine.getName());
            medDetailsTextView.setText(StringUtils.getNonEmptyString(medicine.getDetails(), notAvailable));
            currentStockTextView.setText(String.format(xUnitsAvailable,
                    StringUtils.dropDecimalIfRoundNumber(medicine.getCurrentStock()), medicine.getMedicationUnit()));
            medPhotoImageView.setImageBitmap(ImageUtils.getNonEmptyImage(medicine.getPhoto(), defaultMedicinePhoto));

            medicineDetailsView.setVisibility(View.VISIBLE);
            setHasOptionsMenu(true);
        } else {
            medicineDetailsView.setVisibility(View.GONE);
            setHasOptionsMenu(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_delete_options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent intent = new Intent(application, AddEditMedicineActivity.class);
                intent.putExtra(MedicineListFragment.KEY_MEDICINE, medicine);
                startActivity(intent);
                break;
            case R.id.menu_delete:
                if (dualPanel) {
                    assert medicineListFragment != null;
                    medicineListFragment.deleteMedicineAndUpdateView(medicine);
                } else {
                    medicine.setDeleted(true);
                    medicineController.save(medicine);

                    Intent medicineListActivityIntent = new Intent(application, MedicineListActivity.class);
                    startActivity(medicineListActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().finish();
                }
                break;
            case android.R.id.home:
                startActivity(new Intent(application, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }

        return false;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
}
