package com.cryptic.imed.activity.appointment;

import android.app.Application;
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
import com.cryptic.imed.controller.PharmacyController;
import com.cryptic.imed.domain.Pharmacy;
import com.cryptic.imed.util.adapter.Filterable;
import com.cryptic.imed.util.adapter.FilterableArrayAdapter;
import com.cryptic.imed.util.adapter.TextWatcherAdapter;
import com.cryptic.imed.util.view.CompatibilityUtils;
import com.cryptic.imed.util.view.TwoLineListItemView;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import java.util.Comparator;

/**
 * @author sharafat
 */
@ContentView(R.layout.list_container)
public class PickPharmacyActivity extends RoboActivity {
    public static final String KEY_SELECTED_PHARMACY = "selected_pharmacy";

    @Inject
    private Application application;
    @Inject
    private LayoutInflater layoutInflater;
    @Inject
    private PharmacyController pharmacyController;

    @InjectView(R.id.list_container)
    private LinearLayout linearLayout;
    @InjectView(R.id.filter_input)
    private EditText filterInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutInflater.inflate(R.layout.list_view, linearLayout, true);

        final ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(new PharmacyListAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pharmacy selectedPharmacy = (Pharmacy) listView.getAdapter().getItem(position);
                setResult(RESULT_OK, getIntent().putExtra(KEY_SELECTED_PHARMACY, selectedPharmacy));
                finish();
            }
        });

        filterInput.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                ((PharmacyListAdapter) listView.getAdapter()).getFilter().filter(s);
            }
        });

        CompatibilityUtils.setHomeButtonEnabled(true, this);
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
