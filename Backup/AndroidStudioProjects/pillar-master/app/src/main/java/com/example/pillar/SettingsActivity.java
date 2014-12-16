package com.example.pillar;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.UUID;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SettingsActivity extends Activity {
//
//	private TextView tvDisplayTime;
//	private Button btnChangeTime;
//	private int hour;
//	private int minute;

	static final int TIME_DIALOG_ID = 999;


	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter btAdapter = null;
//	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;

	// Well known SPP UUID
	private static final UUID MY_UUID =
			UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// Insert your server's MAC address
	private static String address = "00:00:00:00:00:00";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.d(TAG, "In onCreate()");
		setContentView(R.layout.settings_view);

//		setCurrentTimeOnView();
//		addListenerOnButton(); 
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		checkBTState();

		 Button button = (Button) findViewById(R.id.set_button);
         button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
            	// This method will be executed once the timer is over
 				Intent i = new Intent(SettingsActivity.this, ConnectionActivity.class);
 				startActivity(i);
 				// close this activity
 				finish();

             }
         });	
		

	}


	private void checkBTState() {
		// Check for Bluetooth support and then check to make sure it is turned on

		// Emulator doesn't support Bluetooth and will return null
		if(btAdapter==null) { 
			errorExit("Fatal Error", "Bluetooth Not supported. Aborting.");
		} else {
			if (btAdapter.isEnabled()) {
				//  Log.d(TAG, "...Bluetooth is enabled...");
			} else {
				//Prompt user to turn on Bluetooth
				Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	private void errorExit(String title, String message){
		Toast msg = Toast.makeText(getBaseContext(),
				title + " - " + message, Toast.LENGTH_SHORT);
		msg.show();
		finish();
	}

	private void sendData(String message) {
		byte[] msgBuffer = message.getBytes();

		//  Log.d(TAG, "...Sending data: " + message + "...");

		try {
			outStream.write(msgBuffer);
		} catch (IOException e) {
			String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
			if (address.equals("00:00:00:00:00:00")) 
				msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
			msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

			errorExit("Fatal Error", msg);       
		}
	}






	// display current time
//	public void setCurrentTimeOnView() {
//
//		tvDisplayTime = (TextView) findViewById(R.id.tvTime);
//		//timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
//
//		final Calendar c = Calendar.getInstance();
//		hour = c.get(Calendar.HOUR_OF_DAY);
//		minute = c.get(Calendar.MINUTE);
//
//		// set current time into textview
//		tvDisplayTime.setText(new StringBuilder().append(pad(hour)).append(":")
//				.append(pad(minute)));
//	}
//
//	public void addListenerOnButton() {
//
//		btnChangeTime = (Button) findViewById(R.id.btnChangeTime);
//
//		btnChangeTime.setOnClickListener(new OnClickListener() {
//
//			@SuppressWarnings("deprecation")
//			@Override
//			public void onClick(View v) {
//
//				showDialog(TIME_DIALOG_ID);
//
//			}
//
//		});
//
//	}

//	@Override
//	protected Dialog onCreateDialog(int id) {
//		switch (id) {
//		case TIME_DIALOG_ID:
//			// set time picker as current time
//			return new TimePickerDialog(this, timePickerListener, hour, minute,
//					false);
//
//		}
//		return null;
//	}
//
//	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
//		public void onTimeSet(TimePicker view, int selectedHour,
//				int selectedMinute) {
//			hour = selectedHour;
//			minute = selectedMinute;
//
//			// set current time into textview
//			tvDisplayTime.setText(new StringBuilder().append(pad(hour))
//					.append(":").append(pad(minute)));
//
//		}
//	};
//
//	private static String pad(int c) {
//		if (c >= 10)
//			return String.valueOf(c);
//		else
//			return "0" + String.valueOf(c);
//	}



}