package com.thoughtworks.karthikintern.reminderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by karthikintern on 9/16/14.
 */
public class NotifResult extends Activity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notif_result);
        Intent switchback=new Intent(NotifResult.this,ProfileActivity.class);
        startActivity(switchback);
        finish();
    }
}
