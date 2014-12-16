package com.medsreminder.logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;

import android.content.Context;


public class Alarm implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public final static String SERIALIZED_FILENAME ="alarms.bin";
	
	private String medName;
	private String medDesciption;
	private Calendar initialDate;
	private Time initialTime;
	private boolean monRepeat;
	private boolean tueRepeat;
	private boolean wedRepeat;
	private boolean thuRepeat;
	private boolean friRepeat;
	private boolean satRepeat;
	private boolean sunRepeat;
	private int hourInterval;
	private int minutesInterval;
	private int dose;
	private String imagePath;
	

	public String getMedName() {
		return medName;
	}

	public void setMedName(String medName) {
		this.medName = medName;
	}
	
	public String getMedDesciption() {
		return medDesciption;
	}

	public void setMedDesciption(String medDesciption) {
		this.medDesciption = medDesciption;
	}

	public Calendar getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Calendar initialDate) {
		this.initialDate = initialDate;
	}

	public Time getInitialTime() {
		return initialTime;
	}

	public void setInitialTime(Time initialTime) {
		this.initialTime = initialTime;
	}

	public int getHourInterval() {
		return hourInterval;
	}

	public void setHourInterval(int hourInterval) {
		this.hourInterval = hourInterval;
	}

	public int getMinutesInterval() {
		return minutesInterval;
	}

	public void setMinutesInterval(int minutesInterval) {
		this.minutesInterval = minutesInterval;
	}
	
	public int getDose() {
		return dose;
	}

	public void setDose(int dose) {
		this.dose = dose;
	}

	public boolean isMonRepeat() {
		return monRepeat;
	}

	public void setMonRepeat(boolean monRepeat) {
		this.monRepeat = monRepeat;
	}

	public boolean isTueRepeat() {
		return tueRepeat;
	}

	public void setTueRepeat(boolean tueRepeat) {
		this.tueRepeat = tueRepeat;
	}

	public boolean isWedRepeat() {
		return wedRepeat;
	}

	public void setWedRepeat(boolean wedRepeat) {
		this.wedRepeat = wedRepeat;
	}

	public boolean isThuRepeat() {
		return thuRepeat;
	}

	public void setThuRepeat(boolean thuRepeat) {
		this.thuRepeat = thuRepeat;
	}

	public boolean isFriRepeat() {
		return friRepeat;
	}

	public void setFriRepeat(boolean friRepeat) {
		this.friRepeat = friRepeat;
	}

	public boolean isSatRepeat() {
		return satRepeat;
	}

	public void setSatRepeat(boolean satRepeat) {
		this.satRepeat = satRepeat;
	}

	public boolean isSunRepeat() {
		return sunRepeat;
	}

	public void setSunRepeat(boolean sunRepeat) {
		this.sunRepeat = sunRepeat;
	}
	
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public boolean[] getAlarmDaysArray(){
		boolean[] dayArray = new boolean[7];
		dayArray[0] = this.monRepeat;
		dayArray[1] = this.tueRepeat;
		dayArray[2] = this.wedRepeat;
		dayArray[3] = this.thuRepeat;
		dayArray[4] = this.friRepeat;
		dayArray[5] = this.satRepeat;
		dayArray[6] = this.sunRepeat;
		
		return dayArray;
	}

	public void serializeClass(Context c){
		
		try {
			
			FileOutputStream fos = c.openFileOutput(SERIALIZED_FILENAME,Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(this);
			
			oos.flush();
			oos.close();
			
			fos.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Alarm loadSerializedClass(Context c){
		
		Alarm alarm = new Alarm();
		
		try {
			FileInputStream fis = c.openFileInput(SERIALIZED_FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			alarm = (Alarm)ois.readObject();
			
			ois.close();
			
			fis.close();
			
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return alarm;
		
	}

}



