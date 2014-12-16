package com.medsreminder.notification;

import com.medsreminder.ui.MedNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MedNotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			Bundle bundle = intent.getExtras();
			
			String photoPath = bundle.getString("photo_path");
			String medName = bundle.getString("med_name");
			String medDose = bundle.getString("med_dose");
			
			Intent newIntent = new Intent(context, MedNotification.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			newIntent.putExtra("photo_path", photoPath);
			newIntent.putExtra("med_name", medName);
			newIntent.putExtra("med_dose", medDose);
			
			context.startActivity(newIntent);
			
		} catch (Exception e) {
			Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
			e.printStackTrace();

		}

	}

}
