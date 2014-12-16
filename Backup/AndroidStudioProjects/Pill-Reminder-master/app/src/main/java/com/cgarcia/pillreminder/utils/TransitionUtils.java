package com.cgarcia.pillreminder.utils;

import android.app.Activity;
import android.content.Intent;

import com.cgarcia.pillreminder.activity.CreateDrugFormActivity;
import com.cgarcia.pillreminder.activity.CreateTreatmentFormActivity;
import com.cgarcia.pillreminder.activity.DrugListActivity;
import com.cgarcia.pillreminder.activity.OptionsActivity;
import com.cgarcia.pillreminder.domain.Treatment;

public class TransitionUtils {

	public static String SELECTED_TREATMENT = "SELECTED_TREATMENT";

	public static void goToDrugList(Activity source, Treatment treatment) {
		Intent target = new Intent(source, DrugListActivity.class);
		target.putExtra(SELECTED_TREATMENT, treatment);
		source.startActivity(target);
	}

	public static void goToCreateTreatment(
			Activity source) {
		Intent target = new Intent(source, CreateTreatmentFormActivity.class);
		source.startActivity(target);
	}

	public static void goToCreateDrug(Activity source) {
		Intent target = new Intent(source, CreateDrugFormActivity.class);
		source.startActivity(target);
	}

	public static void goToOptions(Activity source) {
		Intent target = new Intent(source, OptionsActivity.class);
		source.startActivity(target);
	}
}
