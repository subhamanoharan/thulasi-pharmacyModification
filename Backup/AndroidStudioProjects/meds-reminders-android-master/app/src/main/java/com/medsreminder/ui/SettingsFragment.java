package com.medsreminder.ui;

import com.example.medsreminder.R;


import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.preferences);
	    }
	 
	 public void onResume() {
		    super.onResume();
		    getView().setBackgroundColor(Color.WHITE);
		}

}
