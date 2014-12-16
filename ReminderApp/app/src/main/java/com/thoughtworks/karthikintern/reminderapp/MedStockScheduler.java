package com.thoughtworks.karthikintern.reminderapp;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.karthikintern.reminderapp.JsonParser.Jparser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.support.v4.content.WakefulBroadcastReceiver.completeWakefulIntent;

/**
 * Created by karthikintern on 9/24/14.
 */
public class MedStockScheduler extends IntentService {

    public static final String TAG="Scheduling Service";
    SharedPreferences pref;
    String phno,str_dosage,jsonmed,email;
    Date lastStockUpdate=new Date();
    SharedPreferences.Editor e;
    private static String url = "http://10.0.3.2:8082/pullMed";
    public MedStockScheduler(){
        super("MedStockScheduler");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Jparser jParser = new Jparser();
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        phno = pref.getString("phno",null);
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();
        if(isInternetPresent) {
            if (phno != null) {
                //JSONObject json = jParser.getJSONFromUrl(url,phno);
                //checkStock(json);
                JSONObject json1 = jParser.getJSONFromUrl("http://10.0.3.2:8082/api/resetstock", phno);
                lastStockUpdate=new Date();
                checkStock(json1);
            }
        }
        else
        {
            notify("No internet connection/Server is down");

        }
        Log.i("Wakeful Completed","Wakeful Complete");
        completeWakefulIntent(intent);
        //stockLow();
    }
    private void checkStock(JSONObject json) {
        JSONArray android = null;
        String outOfStock="";
        boolean isOutOfStock=false;
        String TAG_MED = "meds";
        String TAG_MED_NAME = "name";
        String TAG_MED_START_DATE = "start_date";
        String TAG_MED_END_DATE = "end_date";
        String TAG_MED_STOCK = "stock";
        String TAG_MED_DESC = "desc";
        String TAG_MED_DOSAGE = "dosage";
        try {
            android = json.getJSONArray(TAG_MED);
        } catch (NullPointerException j) {
            Intent imback = new Intent(this, ProfileActivity.class);
            imback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(imback);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        try {
            String medjson = android.toString();
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("medjson", medjson);
            edit.commit();
        } catch (NullPointerException j) {
            Intent imback = new Intent(this, ProfileActivity.class);
            imback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(imback);
        }
        try {
            for (int i = 0; i < android.length(); i++) {
                JSONObject c = android.getJSONObject(i);
                // Storing  JSON item in a Variable
                String str_name = c.getString(TAG_MED_NAME);
                String str_start = c.getString(TAG_MED_START_DATE);
                String str_end = c.getString(TAG_MED_END_DATE);
                String str_stock = c.getString(TAG_MED_STOCK);
                String str_desc = c.getString(TAG_MED_DESC);
                String str_dosage = c.getString(TAG_MED_DOSAGE);
                /*try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    Date start_date = (Date) dateFormat.parse(str_start);
                    Date end_date=(Date)dateFormat.parse(str_end);
                }
                catch (ParseException e1) {
                   e1.printStackTrace();
                }*/
                if (Integer.parseInt(str_stock) <= Integer.parseInt(str_dosage)) {
                    isOutOfStock=true;
                    outOfStock+=" "+str_name;

                }
            }
            if(isOutOfStock)
            notify("Low Stock!!");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void notify(String text) {
        NotificationCompat.Builder notification;
        PendingIntent pIntent;
        NotificationManager manager;
        Intent resultIntent;
        TaskStackBuilder stackBuilder;
        notification = new NotificationCompat.Builder(MedStockScheduler.this);
        //Title for Notification
        //("Thulasi Pharmacy on Mobile");
        notification.setContentTitle("Thulasi Pharmacy on Mobile");
        //Message in the Notification
        notification.setContentText(text);
        //Alert shown when Notification is received
        notification.setTicker("Thulasi Pharmacy  - Notification");
        //Icon to be set on Notification
        notification.setSmallIcon(R.drawable.ic_launcher);
        //Creating new Stack Builder
        stackBuilder = TaskStackBuilder.create(MedStockScheduler.this);
        stackBuilder.addParentStack(PresView.class);
        //Intent which is opened when notification is clicked
        resultIntent = new Intent(MedStockScheduler.this, PresView.class);
        stackBuilder.addNextIntent(resultIntent);
        pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pIntent);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }
}

