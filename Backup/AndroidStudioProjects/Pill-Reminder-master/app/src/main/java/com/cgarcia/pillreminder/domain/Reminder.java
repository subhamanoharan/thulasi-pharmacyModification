package com.cgarcia.pillreminder.domain;

import java.io.Serializable;
import java.util.Date;

public class Reminder implements Serializable {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 2048376548398323230L;

	protected Date _startDate;

	protected Date _endDate;

	protected int _firstTimeHour;

	protected int _firstTimeMin;

	protected int _timesADay;

	public Reminder() {

	}

	public Reminder(Date startDate, Date endDate, int firstTimeHour,
			int firstTimeMin, int timesADay) {
		super();
		_startDate = startDate;
		_endDate = endDate;
		_firstTimeHour = firstTimeHour;
		_firstTimeMin = firstTimeMin;
		_timesADay = timesADay;
	}

	public Date getStartDate() {
		return _startDate;
	}

	public void setStartDate(Date startDate) {
		_startDate = startDate;
	}

	public Date getEndDate() {
		return _endDate;
	}

	public void setEndDate(Date endDate) {
		_endDate = endDate;
	}

	public int getFirstTimeHour() {
		return _firstTimeHour;
	}

	public void setFirstTimeHour(int firstTimeHour) {
		_firstTimeHour = firstTimeHour;
	}

	public int getFirstTimeMin() {
		return _firstTimeMin;
	}

	public void setFirstTimeMin(int firstTimeMin) {
		_firstTimeMin = firstTimeMin;
	}

	public int getTimesADay() {
		return _timesADay;
	}

	public void setTimesADay(int timesADay) {
		_timesADay = timesADay;
	}

}
