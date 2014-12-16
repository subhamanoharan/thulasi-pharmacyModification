package com.thoughtworks.karthikintern.reminderapp;

/**
 * Created by karthikintern on 9/3/14.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class ProfileActivity extends Activity {
    SharedPreferences pref;
    String token,grav,oldpasstxt,newpasstxt;
    WebView web;
    Button chgpass,chgpassfr,cancel,logout,pres;
    Dialog dlg;
    EditText oldpass,newpass;
    List<NameValuePair> params;
    NotificationCompat.Builder notification;
    PendingIntent pIntent;
    NotificationManager manager;
    Intent resultIntent;
    TaskStackBuilder stackBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecvAlarm ra = new RecvAlarm();
        setContentView(R.layout.activity_settings);
        web = (WebView)findViewById(R.id.webView);
        chgpass = (Button)findViewById(R.id.chgbtn);
        logout = (Button)findViewById(R.id.logout);
        pres=(Button)findViewById(R.id.BtnViewPres);
        startNotification();
        pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent viewPresc = new Intent(ProfileActivity.this,PresView.class);
                startActivity(viewPresc);
                finish();
            }



    });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                //edit.putString("token", "");
                edit.clear();
                edit.commit();
                Intent loginactivity = new Intent(ProfileActivity.this,LoginActivity.class);
                startActivity(loginactivity);
                finish();
            }
        });
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        grav = pref.getString("grav", "");
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.loadUrl(grav);

        chgpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg = new Dialog(ProfileActivity.this);
                dlg.setContentView(R.layout.chgpassword_frag);
                dlg.setTitle("Change Password");
                chgpassfr = (Button) dlg.findViewById(R.id.chgbtn);
                chgpassfr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        oldpass = (EditText) dlg.findViewById(R.id.oldpass);
                        newpass = (EditText) dlg.findViewById(R.id.newpass);
                        oldpasstxt = oldpass.getText().toString();
                        newpasstxt = newpass.getText().toString();
                        params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("oldpass", oldpasstxt));
                        params.add(new BasicNameValuePair("newpass", newpasstxt));
                        params.add(new BasicNameValuePair("id", token));
                        ServerRequest sr = new ServerRequest();
                        //    JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/chgpass",params);
                        JSONObject json = sr.getJSON("http://10.0.2.2:8082/api/chgpass", params);
                        if (json != null) {
                            try {
                                String jsonstr = json.getString("response");
                                if (json.getBoolean("res")) {
                                    dlg.dismiss();
                                    Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                cancel = (Button) dlg.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlg.dismiss();
                    }
                });
                dlg.show();
            }
        });



    }
    protected void startNotification() {
        // TODO Auto-generated method stub
        //Creating Notification Builder
        notification = new NotificationCompat.Builder(ProfileActivity.this);
        //Title for Notification
        notification.setContentTitle("Thulasi Pharmacy on Mobile");
        //Message in the Notification
        notification.setContentText("Successfully logged into service");
        //Alert shown when Notification is received
        notification.setTicker("Thulasi Pharmacy  - Notification");
        //Icon to be set on Notification
        notification.setSmallIcon(R.drawable.ic_launcher);
        //Creating new Stack Builder
        stackBuilder = TaskStackBuilder.create(ProfileActivity.this);
        stackBuilder.addParentStack(NotifResult.class);
        //Intent which is opened when notification is clicked
        resultIntent = new Intent(ProfileActivity.this, NotifResult.class);
        stackBuilder.addNextIntent(resultIntent);
        pIntent =  stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pIntent);
        manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }
}