package com.medsreminder.ui;

import com.example.medsreminder.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CustomMedArrayAdapter extends ArrayAdapter<String>{
	private final Context context;
	private final String[] values;

	public CustomMedArrayAdapter(Context context, String[] values) {
		super(context, R.layout.rowlayout, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
		
		//Set text to TextView within the row layout
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		textView.setText(values[position]);
	
		//To be able to identify row index on click event
		ImageButton deleteButton = (ImageButton)rowView.findViewById(R.id.deleteMedButton);
		deleteButton.setTag(position);
		
		return rowView;
	}
	
	
	
	
}

