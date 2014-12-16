package com.cgarcia.pillreminder.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cgarcia.pillreminder.R;
import com.cgarcia.pillreminder.domain.Drug;

public class DrugAdapter extends ArrayAdapter<Drug> {

	public DrugAdapter(Context context, List<Drug> drugs) {
		super(context, R.layout.drug_list_item, drugs);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = (View) convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.drug_list_item, parent, false);
		}
		TextView nameView = (TextView) rowView.findViewById(R.id.drug_name);
		Drug currDrug = getItem(position);
		nameView.setText(currDrug.getName());
		if (currDrug.getReminder() != null) {
			TextView periodView = (TextView) rowView
					.findViewById(R.id.drug_times_a_day);
			periodView.setText(currDrug.getReminder().getTimesADay()
					+ " times a day");
		}
		return rowView;
	}
}
