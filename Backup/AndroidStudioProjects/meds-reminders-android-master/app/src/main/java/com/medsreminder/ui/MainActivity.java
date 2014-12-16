package com.medsreminder.ui;

import java.util.Calendar;

import com.example.medsreminder.R;

import com.medsreminder.notification.MedNotificationReceiver;

import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;


//This class was used for testing purposes only
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}
	
	public void sendMessage(View view){
		
		// get a Calendar object with current time
		 Calendar cal = Calendar.getInstance();
		 // add 5 minutes to the calendar object
		 cal.add(Calendar.SECOND, 5);
		 Intent intent = new Intent(this , MedNotificationReceiver.class);
		 intent.setAction("com.example.medsreminder.MedNotificationReceiver");
		 intent.putExtra("alarm_message", "O'Doyle Rules!");
		 
		 // In reality, you would want to have a static variable for the request code instead of 192837
		 PendingIntent sender = PendingIntent.getBroadcast(this, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		 
		 // Get the AlarmManager service
		 android.app.AlarmManager am = (android.app.AlarmManager) getSystemService(ALARM_SERVICE);
		 am.set(android.app.AlarmManager.RTC_WAKEUP,  cal.getTimeInMillis(), sender);
		 
		 //alarm2
		 Calendar cal2 = Calendar.getInstance();
		 // add 5 minutes to the calendar object
		 cal2.add(Calendar.SECOND, 10);
		 
		 Intent intent2 = new Intent(this  , MedNotificationReceiver.class);
		 intent2.setAction("com.example.medsreminder.MedNotificationReceiver");
		 intent2.putExtra("alarm_message", "O'Doyle Rules2222222222!");
		 
		 // In reality, you would want to have a static variable for the request code instead of 192837
		 PendingIntent sender2 = PendingIntent.getBroadcast(this, 192837+1, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		 
		 // Get the AlarmManager service
		 //android.app.AlarmManager am2 = (android.app.AlarmManager) getSystemService(ALARM_SERVICE);
		 am.set(android.app.AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), sender2);
		
		 Toast.makeText(this, "Alarms Created", Toast.LENGTH_SHORT).show();
	}
	
	
	public void cancelMessage(View view){
		 
		Intent intent = new Intent(this , MedNotificationReceiver.class);
		intent.setAction("com.example.medsreminder.MedNotificationReceiver");
		 //intent.putExtra("alarm_message", "O'Doyle Rules!");
		 
		PendingIntent alarmIntent = PendingIntent.getBroadcast(this , 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		android.app.AlarmManager am = (android.app.AlarmManager) getSystemService(ALARM_SERVICE);
		am.cancel(alarmIntent);
		
		Intent intent2 = new Intent(this  , MedNotificationReceiver.class);
		intent2.setAction("com.example.medsreminder.MedNotificationReceiver");
		PendingIntent alarmIntent2 = PendingIntent.getBroadcast(this , 192837+1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		am.cancel(alarmIntent2); 
		
		
		Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
	}
	
}
