package com.medsreminder.logic;

import java.io.Serializable;

public class Time implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int hour;
	private int minutes;
	private int seconds;

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
}
