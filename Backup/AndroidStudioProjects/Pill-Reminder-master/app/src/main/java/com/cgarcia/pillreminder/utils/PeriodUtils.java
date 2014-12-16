package com.cgarcia.pillreminder.utils;

import com.cgarcia.pillreminder.domain.Treatment;

public class PeriodUtils {

	public static String getTreatmentPeriodStr(Treatment treatment) {
		return treatment.getName() + ".Period";
	}
}
