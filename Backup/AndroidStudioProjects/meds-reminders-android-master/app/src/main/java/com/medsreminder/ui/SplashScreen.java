package com.medsreminder.ui;

import com.example.medsreminder.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashScreen extends Activity {

	//Splash Screen Timer
	private static int S_TIME_OUT = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		new Handler().postDelayed(
				new Runnable(){
					
					@Override
					public void run(){
						Intent i = new Intent(SplashScreen.this, ListViewActivity.class);
				
						startActivity(i);
						
						finish();
					}
				}
				, S_TIME_OUT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}
	
	

}
