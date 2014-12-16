package com.medsreminder.ui;

import java.util.Calendar;

import com.example.medsreminder.R;


import com.medsreminder.camera.PhotoManager;
import com.medsreminder.notification.MedNotificationReceiver;
import com.medsreminder.notification.WakeLocker;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

public class MedNotification extends Activity {
	
	
	private PhotoManager photoManager;
	private static final int IMAGE_VIEW_WIDTH = 382*2;//254;//382;//254*2;
	private static final int IMAGE_VIEW_HEIGHT = 144*2;//96;//144;//96*2;
	
	public static final String WAKE_TAG = "MY_TAG";//96;//144;//96*2;
	
	ImageView imageViewMedicine;
	TextView textViewMedName;
	TextView textViewMedDose;
	Button stopButton;
	
	MediaPlayer mediaPlayer;
	String medName;
	String medDose;
	String photoPath;
	
	SharedPreferences shareprefs;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_med_notification);
		
		WakeLocker.acquire(this);
		
		//Change Background Color
		shareprefs = PreferenceManager.getDefaultSharedPreferences(this);
		String prefColor = shareprefs.getString("color", "");
	
		View view = this.getWindow().getDecorView();
	    view.setBackgroundColor(this.getUserColor(Integer.parseInt(prefColor)));
		
		//Set Medicine Image
		imageViewMedicine = (ImageView)findViewById(R.id.imgMed);
		photoManager = new PhotoManager();
		photoPath = getIntent().getStringExtra("photo_path");
		photoManager.setPhotoPath(photoPath);
		imageViewMedicine.setImageBitmap(photoManager.GetBitmap(IMAGE_VIEW_WIDTH, IMAGE_VIEW_HEIGHT));
		
		//Set Medicine Name
		textViewMedName = (TextView)findViewById(R.id.txtMedName);
		medName = getIntent().getStringExtra("med_name");
		textViewMedName.setText("Medicine Name: " + medName);
		
		//Set Medicine Dose
		textViewMedDose = (TextView)findViewById(R.id.txtMedDose);
		medDose = getIntent().getStringExtra("med_dose");
		textViewMedDose.setText("Medicine Dose: " + medDose);
		
		
		//start playing sound of alarm
		String pref = shareprefs.getString("ring", "");
		
		int soundId = this.getAlarmSoundId(Integer.parseInt(pref));
		mediaPlayer = MediaPlayer.create(this, soundId);
		mediaPlayer.setLooping(true);
		
		mediaPlayer.start(); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.med_notification, menu);
		return true;
	}
	
	public void stopPlay(View view){
		if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
		}
		else if(!mediaPlayer.isPlaying()){
			mediaPlayer.stop();
			mediaPlayer.release();
			WakeLocker.release();
			Toast.makeText(this, "Alarm Released", Toast.LENGTH_LONG).show();
			this.finish();
		}
		
		
		
	}
	
	
	public void snoozePlay(View view){
		//Set alarm time
		Calendar cal = Calendar.getInstance();
	
		String pref = shareprefs.getString("snooze", "");
		int snoozeTime = this.getSnoozeTime(Integer.parseInt(pref));
		
		cal.add(Calendar.SECOND, snoozeTime);
		
		Intent intent = new Intent(view.getContext() , MedNotificationReceiver.class);
		intent.setAction("com.example.medsreminder.MedNotificationReceiver");
		
		intent.putExtra("photo_path", photoPath);
		intent.putExtra("med_name", medName);
		intent.putExtra("med_dose", medDose);
		 
		 PendingIntent sender = PendingIntent.getBroadcast(view.getContext(), 3000/*SCHEDULE_ALARM_ID + i*/ , intent, PendingIntent.FLAG_UPDATE_CURRENT);
		 
		 // Get the AlarmManager service
		 android.app.AlarmManager am = (android.app.AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
		 am.set(android.app.AlarmManager.RTC_WAKEUP,  cal.getTimeInMillis(), sender);
		 
		 Toast.makeText(this, "Alarm Snoozed, it will sound again in " + String.valueOf(snoozeTime) + " seconds", Toast.LENGTH_LONG).show();
		 
		 //Close the activity
		 this.finish();
	}
	
	private int getAlarmSoundId(int userPref){
		switch(userPref){
		
		case 1:
			return R.raw.sirennoise;
		case 2:
			return R.raw.buzzer;
		case 3:
			return R.raw.door;
		case 4:
			return R.raw.bell;
		default:
			return R.raw.sirennoise;
		}
		
	}
	
	private int getUserColor(int userPref){
		switch(userPref){
		
		case 1:
			return Color.WHITE;
		case 2:
			return Color.WHITE;
		case 3:
			return Color.GRAY;
		case 4:
			return Color.RED;
		default:
			return Color.BLUE;
		}
		
	}
	
	private int getSnoozeTime(int userPref){
		switch(userPref){
		
		case 1:
			return 60;
		case 2:
			return 60*5;
		case 3:
			return 60*10;
		case 4:
			return 60*15;
		default:
			return 60*30;
		}
		
	}
}
