package com.cgarcia.pillreminder.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.cgarcia.pillreminder.R;
import com.cgarcia.pillreminder.adapter.TreatmentAdapter;
import com.cgarcia.pillreminder.dao.MockTreatmentDao;
import com.cgarcia.pillreminder.domain.Treatment;
import com.cgarcia.pillreminder.utils.TransitionUtils;

public class TreatmentListActivity extends Activity implements
		OnItemClickListener {

	private List<Treatment> _treatments;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.treatment_list);

		ListView listView = (ListView) findViewById(R.id.treatment_list_view);

		if (_treatments == null) {
			_treatments = MockTreatmentDao.getTreatments();
		}

		TreatmentAdapter ta = new TreatmentAdapter(getApplicationContext(),
				_treatments);
		listView.setAdapter(ta);
		listView.setOnItemClickListener(this);

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(getApplicationContext(), "Clicked element: " + position,
				Toast.LENGTH_SHORT).show();
		TransitionUtils.goToDrugList(this, _treatments.get(position));
	}

	/* */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.treatment_list_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_treatment:
			TransitionUtils.goToCreateTreatment(this);
			break;
		case R.id.options:
			TransitionUtils.goToOptions(this);
			break;
		default:
			return false;
		}
		return true;
	}
}