package com.cgarcia.pillreminder.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.cgarcia.pillreminder.R;
import com.cgarcia.pillreminder.adapter.DrugAdapter;
import com.cgarcia.pillreminder.domain.Drug;

public class SearchableDrugActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.drug_search_results);

	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      List<Drug> foundDrugs = executeSearch(query);
	      ListView lv = (ListView)findViewById(R.id.drug_search_results_view);
	      lv.setAdapter(new DrugAdapter(getApplicationContext(), foundDrugs));
	    }
	}

	private List<Drug> executeSearch(String query){
		// TODO
		List<Drug> foundDrugs = new ArrayList<Drug>();
		Drug d1 = new Drug();
		d1.setName("Result drug");
		foundDrugs.add(d1);
		return foundDrugs;
	}

}
