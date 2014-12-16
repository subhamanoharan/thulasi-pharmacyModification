package com.medsreminder.logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.medsreminder.notification.MedNotificationReceiver;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmManager implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public final static String SERIALIZED_FILENAME ="alarms.bin";
	private List<Alarm> alarms;
	
	private final static int SCHEDULE_ALARM_ID = 0;
	private int scheduledAlarmsCount = 0;
	
	public AlarmManager(){
		alarms = new ArrayList<Alarm>();
	}

	public List<Alarm> getAlarms() {
		return alarms;
	}

	public void setAlarms(List<Alarm> alarms) {
		this.alarms = alarms;
	}
	
	public void AddAlarm(Alarm alarm){
		alarms.add(alarm);
	}
	
	public Alarm GetAlarm(int position){
		return alarms.get(position);
	}
	
	public void DeleteAlarm(int position){
		alarms.remove(position);
	}
	
	public int AlarmCount(){
		return alarms.size();
	}
	
	public String[] GetAlarmNames(){
		
		String [] values = new String [alarms.size()];
		for(int i =0;i < alarms.size();i++){
			values[i] = alarms.get(i).getMedName();
		}
		
		return values;
		
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
	
	public AlarmManager loadSerializedClass(Context c){
		
		AlarmManager alarmManager = new AlarmManager();
		
		try {
			FileInputStream fis = c.openFileInput(SERIALIZED_FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			alarmManager = (AlarmManager)ois.readObject();
			
			ois.close();
			
			fis.close();
			
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return alarmManager;
	}
	
	
	//Alarm scheduling with AlarmManager
	public void ScheduleAlarms(Context c){
		
		for (int i = 0; i < alarms.size(); i++){
			Alarm al = alarms.get(i);
			for( boolean dayAlarm : al.getAlarmDaysArray() ){
				if(dayAlarm == true){ 
					//Set alarm time
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(System.currentTimeMillis());
					cal.set(Calendar.HOUR_OF_DAY, al.getInitialTime().getHour());
					cal.set(Calendar.MINUTE, al.getInitialTime().getMinutes());
					cal.set(Calendar.SECOND, 0);
					
					Calendar cal2 = Calendar.getInstance();
					if(cal.after(cal2)){  //only if greater
					
						Intent intent = new Intent(c , MedNotificationReceiver.class);
						intent.setAction("com.example.medsreminder.MedNotificationReceiver");
						
						intent.putExtra("photo_path", al.getImagePath());
						intent.putExtra("med_name", al.getMedName());
						intent.putExtra("med_dose", String.valueOf(al.getDose()));
						 
						 //Start assigning id to each alarm.
						 PendingIntent sender = PendingIntent.getBroadcast(c, SCHEDULE_ALARM_ID + i , intent, PendingIntent.FLAG_UPDATE_CURRENT);
						 
						 if(al.getMinutesInterval() == 0 ){
							 // Get the AlarmManager service
							 android.app.AlarmManager am = (android.app.AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
							 am.set(android.app.AlarmManager.RTC_WAKEUP,  cal.getTimeInMillis(), sender);
						 }
						 else{
							 android.app.AlarmManager am = (android.app.AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
							 am.setRepeating(android.app.AlarmManager.RTC_WAKEUP,  cal.getTimeInMillis(),al.getMinutesInterval()*60*1000, sender);
						 }	 
					}
				}
			}
			 
		}
		
		//set flag
		scheduledAlarmsCount = alarms.size();
			
	}
	
	//Cancel previous alarms	
	public void CancelScheduledAlarms(Context c){
		for (int i = 0; i < scheduledAlarmsCount; i++){
			
			Intent intent = new Intent(c , MedNotificationReceiver.class);
			intent.setAction("com.example.medsreminder.MedNotificationReceiver");
			
			 
			PendingIntent alarmIntent = PendingIntent.getBroadcast(c , SCHEDULE_ALARM_ID + i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			android.app.AlarmManager am = (android.app.AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
			am.cancel(alarmIntent);
		}
	}

}
