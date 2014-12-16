package com.cgarcia.pillreminder.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cgarcia.pillreminder.domain.Drug;
import com.cgarcia.pillreminder.domain.Reminder;
import com.cgarcia.pillreminder.domain.Treatment;

public class MockTreatmentDao {

	private static List<Treatment> MOCK_TREATMENTS = new ArrayList<Treatment>();

	static {
		for (int i = 0; i < 5; i++) {
			Treatment t = new Treatment();
			t.setName("Treatment " + i);
			List<Drug> drugs = new ArrayList<Drug>();
			for (int j = 0; j < 5; j++) {
				Drug d = new Drug();
				d.setName("Drug " + (i + j));
				d.setDescription("Description " + (i + j));
				Reminder r = new Reminder();
				r.setStartDate(new Date());
				r.setEndDate(new Date());
				r.setFirstTimeMin(0);
				r.setFirstTimeHour(0);
				r.setTimesADay(3);
				d.setReminder(r);
				drugs.add(d);
			}
			t.setDrugs(drugs);
			MOCK_TREATMENTS.add(t);
		}
	}

	public static List<Treatment> getTreatments() {
		return MOCK_TREATMENTS;
	}

}
