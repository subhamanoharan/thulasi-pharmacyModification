package com.thoughtworks.karthikintern.reminderapp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtworks.karthikintern.reminderapp.JsonParser.Jparser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by karthikintern on 9/12/14.
 */

public class PresView extends Activity{
    SharedPreferences pref;
    ListView list;
    TextView med_name;
    TextView med_start_date;
    TextView med_end_date;
    TextView med_stock;
    TextView med_desc;
    TextView med_dosage;
    String phno,jsonmed;
    JSONObject j;
    ConnectionDetector cd;
    Button back;


    ArrayList<HashMap<String, String>> medlist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array

    private static String url = "http://10.0.3.2:8082/pullMed";
    //private static String url = "http://10.0.2.2:8082/pullMed";
    //JSON Node Names
    private static final String TAG_MED = "meds";
    private static final String TAG_MED_NAME = "name";
    private static final String TAG_MED_START_DATE = "start_date";
    private static final String TAG_MED_END_DATE = "end_date";
    private static final String TAG_MED_STOCK = "stock";
    private static final String TAG_MED_DESC = "desc";
    private static final String TAG_MED_DOSAGE = "dosage";
    JSONArray android = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pres);
        back= (Button)findViewById(R.id.presBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent profactivity = new Intent(PresView.this, ProfileActivity.class);
                startActivity(profactivity);
                finish();

            }
        });
        //list.addFooterView(new View(this),null,false);
        //list.addHeaderView(new View(this),null,false);
        medlist = new ArrayList<HashMap<String, String>>();
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();
        pref = getSharedPreferences("AppPref",MODE_PRIVATE);


        if(isInternetPresent){
            new JSONParse().execute();
        }
        else if(pref.getString("medjson",null)!=null){
            new JSONOffline().execute();
        }
        else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(PresView.this);

            // Setting Dialog Title
            alertDialog.setTitle("Not Connected to Server");

            // Setting Dialog Message
            alertDialog.setMessage("Please ensure you have a working Internet connection");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.ic_launcher);

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {

                    // Write your code here to invoke YES event

                    Intent imback = new Intent(PresView.this,ProfileActivity.class);
                    dialog.cancel();
                    imback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(imback);


                }
            });



            // Showing Alert Message
            alertDialog.show();
        }
    }

    class JSONOffline extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            med_name = (TextView)findViewById(R.id.TXTMedName);
            med_start_date = (TextView)findViewById(R.id.TXTStart);
            med_end_date = (TextView)findViewById(R.id.TXTEnd);
            med_stock=(TextView)findViewById(R.id.TXTCourse);
            med_desc=(TextView)findViewById(R.id.TXTDesc);
            med_dosage=(TextView)findViewById(R.id.TXTDosage);
            pDialog = new ProgressDialog(PresView.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
       // try{
        protected String doInBackground(String... args) {

            //Jparser jParser = new Jparser();
            // Getting JSON from URL

            pref = getSharedPreferences("AppPref", MODE_PRIVATE);
            phno = pref.getString("phno", null);
            jsonmed = pref.getString("medjson", null);
            return jsonmed;
            /*
            JSONArray json2 = null;
            try {
                JSONArray json1 = new JSONArray(jsonmed);
                json2 = json1;
            } catch (JSONException j) {
                j.printStackTrace();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PresView.this);

                // Setting Dialog Title
                alertDialog.setTitle("Not Connected to Server");

                // Setting Dialog Message
                alertDialog.setMessage("Please ensure you have a working Internet connection");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        // Write your code here to invoke YES event

                        Intent imback = new Intent(PresView.this,ProfileActivity.class);
                        dialog.cancel();
                        imback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(imback);

                    }});}
            //}
            //catch(JSONException j){
            // j.printStackTrace();
            // }


            return json2;*/
        }
        // }}

        @Override
        protected void onPostExecute(String med) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL

                try{android = new JSONArray(med);//json.getJSONArray(TAG_MED);
                 }
                catch(NullPointerException j){
                    Toast.makeText(PresView.this,"Not Connected to Network",Toast.LENGTH_SHORT).show();
                    Intent imback = new Intent(PresView.this,ProfileActivity.class);

                    imback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(imback);
                    finish();
                }
                try {
                    String medjson=android.toString();
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("medjson",medjson);
                    edit.commit();}
                catch(NullPointerException j){
                    Toast.makeText(PresView.this,"Not Connected to Network",Toast.LENGTH_SHORT).show();
                    Intent imback = new Intent(PresView.this,ProfileActivity.class);
                    imback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(imback);
                    finish();
                }

                for(int i = 0; i < android.length(); i++){
                    JSONObject c = android.getJSONObject(i);
                    // Storing  JSON item in a Variable
                    String str_name = c.getString(TAG_MED_NAME);
                    String str_start = c.getString(TAG_MED_START_DATE);
                    String str_end=c.getString(TAG_MED_END_DATE);
                    String str_stock=(c.getInt(TAG_MED_STOCK))+"";
                    String str_desc=c.getString(TAG_MED_DESC);
                    String str_dosage=c.getInt(TAG_MED_DOSAGE)+"";
                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_MED_NAME, str_name);
                    map.put(TAG_MED_START_DATE, str_start);
                    map.put(TAG_MED_END_DATE, str_end);
                    map.put(TAG_MED_STOCK,str_stock);
                    map.put(TAG_MED_DESC,str_desc);
                    map.put(TAG_MED_DOSAGE,str_dosage);
                    medlist.add(map);
                    list=(ListView)findViewById(R.id.med_listView);
                    ListAdapter adapter = new SimpleAdapter(PresView.this, medlist,
                            R.layout.layout_list_pres,
                            new String[] { TAG_MED_NAME,TAG_MED_START_DATE,TAG_MED_END_DATE,TAG_MED_STOCK,TAG_MED_DESC,TAG_MED_DOSAGE }, new int[] {
                            R.id.TXTMedName,R.id.TXTStart, R.id.TXTEnd,R.id.TXTCourse,R.id.TXTDesc,R.id.TXTDosage});
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(PresView.this, "You Clicked at " + medlist.get(+position).get("name"), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(PresView.this,"Not Connected to Network",Toast.LENGTH_SHORT).show();
                Intent switchback= new Intent(PresView.this,ProfileActivity.class);

                switchback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(switchback);
                finish();
            }
        }
    }

   class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            med_name = (TextView)findViewById(R.id.TXTMedName);
            med_start_date = (TextView)findViewById(R.id.TXTStart);
            med_end_date = (TextView)findViewById(R.id.TXTEnd);
            med_stock=(TextView)findViewById(R.id.TXTCourse);
            med_desc=(TextView)findViewById(R.id.TXTDesc);
            med_dosage=(TextView)findViewById(R.id.TXTDosage);
            pDialog = new ProgressDialog(PresView.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            Jparser jParser = new Jparser();
            // Getting JSON from URL
            pref = getSharedPreferences("AppPref",MODE_PRIVATE);
            phno = pref.getString("phno",null);
            jsonmed = pref.getString("medjson",null);
            JSONObject json = jParser.getJSONFromUrl(url,phno);
            return json;
            /*ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();
            if(isInternetPresent){
                JSONObject json = jParser.getJSONFromUrl(url,phno);
                j=json;
                Toast.makeText(PresView.this,"Connecting to Internet",Toast.LENGTH_SHORT);
            }
           else if(jsonmed!=null){

                try {
                    JSONObject json = new JSONObject(jsonmed);
                     j = json;
                    Toast.makeText(PresView.this,"Sharedpref",Toast.LENGTH_SHORT);

                }catch (JSONException j){
                j.printStackTrace();
                }

            }
            else{
                Toast.makeText(PresView.this,"Not Connected to any Network",Toast.LENGTH_SHORT);
                //return json;
            }
            return j;*/
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL

                try{android = json.getJSONArray(TAG_MED);}
                catch(NullPointerException j){
                    Toast.makeText(PresView.this,"Not Connected to Network",Toast.LENGTH_SHORT).show();
                    Intent imback = new Intent(PresView.this,ProfileActivity.class);

                    imback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(imback);
                    finish();
                }
                try {
                String medjson=android.toString();
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("medjson",medjson);
                    edit.commit();}
                catch(NullPointerException j){
                    Toast.makeText(PresView.this,"Not Connected to Network",Toast.LENGTH_SHORT).show();
                    Intent imback = new Intent(PresView.this,ProfileActivity.class);
                    imback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(imback);
                    finish();
                }

                for(int i = 0; i < android.length(); i++){
                    JSONObject c = android.getJSONObject(i);
                    // Storing  JSON item in a Variable
                    String str_name = c.getString(TAG_MED_NAME);
                    String str_start = c.getString(TAG_MED_START_DATE);
                    String str_end=c.getString(TAG_MED_END_DATE);
                    String str_stock=c.getString(TAG_MED_STOCK);
                    String str_desc=c.getString(TAG_MED_DESC);
                    String str_dosage=c.getString(TAG_MED_DOSAGE);
                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_MED_NAME, str_name);
                    map.put(TAG_MED_START_DATE, str_start);
                    map.put(TAG_MED_END_DATE, str_end);
                    map.put(TAG_MED_STOCK,str_stock);
                    map.put(TAG_MED_DESC,str_desc);
                    map.put(TAG_MED_DOSAGE,str_dosage);
                    medlist.add(map);
                    list=(ListView)findViewById(R.id.med_listView);
                    ListAdapter adapter = new SimpleAdapter(PresView.this, medlist,
                            R.layout.layout_list_pres,
                            new String[] { TAG_MED_NAME,TAG_MED_START_DATE,TAG_MED_END_DATE,TAG_MED_STOCK,TAG_MED_DESC,TAG_MED_DOSAGE }, new int[] {
                            R.id.TXTMedName,R.id.TXTStart, R.id.TXTEnd,R.id.TXTCourse,R.id.TXTDesc,R.id.TXTDosage});
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(PresView.this, "You Clicked at " + medlist.get(+position).get("name"), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(PresView.this,"Not Connected to Network",Toast.LENGTH_SHORT).show();
                Intent switchback= new Intent(PresView.this,ProfileActivity.class);
                startActivity(switchback);
                finish();
            }
            catch(NullPointerException e)
            {
                e.printStackTrace();
                Toast.makeText(PresView.this,"Not Connected to Network",Toast.LENGTH_SHORT).show();
                Intent switchback= new Intent(PresView.this,ProfileActivity.class);
                startActivity(switchback);
                finish();
            }
        }
    }
}

/*
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ViewPres extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pres);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_pres, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

AlertDialog.Builder alertDialog = new AlertDialog.Builder(AlertDialogActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

            // Write your code here to invoke YES event
            Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // Write your code here to invoke NO event
            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
            dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
*/