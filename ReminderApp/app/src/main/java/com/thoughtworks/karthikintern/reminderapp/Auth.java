package com.thoughtworks.karthikintern.reminderapp;

/**
 * Created by karthikintern on 9/4/14.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

//import static com.thoughtworks.karthikintern.reminderapp.SaveSharedPref.getSharedPreferences;

public class Auth extends Activity {
protected void onCreate(Bundle savedInstanceState) {
SharedPreferences pref;
pref = getSharedPreferences("AppPref",0);
super.onCreate(savedInstanceState);
if (pref.getString("token",null) != null) {
Intent setactivity = new Intent(Auth.this,ProfileActivity.class);
startActivity(setactivity);
finish();

}
    else{
    Intent setactivity2 = new Intent(Auth.this,LoginActivity.class);
    startActivity(setactivity2);
    finish();
    }
}
}

