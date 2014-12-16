package com.cryptic.imed.activity.prescription;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.cryptic.imed.R;
import com.cryptic.imed.controller.DoctorController;
import com.cryptic.imed.domain.Doctor;
import com.cryptic.imed.util.adapter.Filterable;
import com.cryptic.imed.util.adapter.FilterableArrayAdapter;
import com.cryptic.imed.util.adapter.TextWatcherAdapter;
import com.cryptic.imed.util.photo.util.ImageUtils;
import com.cryptic.imed.util.view.CompatibilityUtils;
import com.cryptic.imed.util.view.TwoLineListItemWithImageView;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.Comparator;

/**
 * @author sharafat
 */
@ContentView(R.layout.list_container)
public class PickDoctorActivity extends RoboActivity {
    public static final String KEY_SELECTED_DOCTOR = "selected_doctor";

    @Inject
    private Application application;
    @Inject
    private LayoutInflater layoutInflater;
    @Inject
    private DoctorController doctorController;

    @InjectView(R.id.list_container)
    private LinearLayout linearLayout;
    @InjectView(R.id.filter_input)
    private EditText filterInput;

    @InjectResource(R.drawable.ic_default_photo)
    private Drawable defaultPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutInflater.inflate(R.layout.list_view, linearLayout, true);

        final ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(new DoctorListAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Doctor selectedDoctor = (Doctor) listView.getAdapter().getItem(position);
                setResult(RESULT_OK, getIntent().putExtra(KEY_SELECTED_DOCTOR, selectedDoctor));
                finish();
            }
        });

        filterInput.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                ((DoctorListAdapter) listView.getAdapter()).getFilter().filter(s);
            }
        });

        CompatibilityUtils.setHomeButtonEnabled(true, this);
    }


    private class DoctorListAdapter extends FilterableArrayAdapter {
        DoctorListAdapter() {
            super(application, 0);

            addAll(doctorController.findByDeleted(false));

            sort(new Comparator<Filterable>() {
                @Override
                public int compare(Filterable lhs, Filterable rhs) {
                    return ((Doctor) lhs).getName().compareTo(((Doctor) rhs).getName());
                }
            });
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Doctor doctor = (Doctor) getItem(position);
            return TwoLineListItemWithImageView.getView(layoutInflater, convertView, parent,
                    doctor.getName(), doctor.getAddress(),
                    ImageUtils.getNonEmptyImage(doctor.getPhoto(), defaultPhoto));
        }
    }
}
