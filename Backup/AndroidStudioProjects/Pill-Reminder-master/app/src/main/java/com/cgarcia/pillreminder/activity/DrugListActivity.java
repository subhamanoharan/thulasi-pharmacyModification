package com.cgarcia.pillreminder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.cgarcia.pillreminder.R;
import com.cgarcia.pillreminder.adapter.DrugAdapter;
import com.cgarcia.pillreminder.domain.Treatment;
import com.cgarcia.pillreminder.utils.TransitionUtils;

public class DrugListActivity extends Activity implements OnItemClickListener {

	private Treatment _selectedTreatment;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.treatment_list);

		ListView listView = (ListView) findViewById(R.id.treatment_list_view);

		if (_selectedTreatment == null) {
			_selectedTreatment = getSelectedTreatment();

		}

		DrugAdapter da = new DrugAdapter(getApplicationContext(),
				_selectedTreatment.getDrugs());
		listView.setAdapter(da);
		listView.setOnItemClickListener(this);

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(getApplicationContext(), "Clicked element: " + position,
				Toast.LENGTH_SHORT).show();
	}

	private Treatment getSelectedTreatment() {
		Bundle extras = getIntent().getExtras();
		Treatment treat = null;
		if (extras != null) {
			treat = (Treatment) extras
					.getSerializable(TransitionUtils.SELECTED_TREATMENT);
		}
		return treat;
	}

	/* */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.drug_list_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.add_drug:
			TransitionUtils.goToCreateDrug(this);
			break;
		default:
			return false;
		}
		return true;
	}
}