package com.cgarcia.pillreminder.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cgarcia.pillreminder.R;
import com.cgarcia.pillreminder.domain.Treatment;
import com.cgarcia.pillreminder.utils.PeriodUtils;

public class TreatmentAdapter extends ArrayAdapter<Treatment> {

	public TreatmentAdapter(Context context, List<Treatment> treatments) {
		super(context, R.layout.treatment_list_item, treatments);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = (View) convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.treatment_list_item, parent,
					false);
		}
		TextView nameView = (TextView) rowView
				.findViewById(R.id.treatment_name);
		Treatment currTreatment = getItem(position);
		nameView.setText(currTreatment.getName());
		TextView periodView = (TextView) rowView
				.findViewById(R.id.treatment_period);
		periodView.setText(PeriodUtils.getTreatmentPeriodStr(currTreatment));
		return rowView;
	}
}
