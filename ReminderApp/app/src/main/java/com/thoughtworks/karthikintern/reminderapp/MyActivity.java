package com.thoughtworks.karthikintern.reminderapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MyActivity extends ActionBarActivity {

    private Button BtnFindNearest;
    private ImageButton BtnFbClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        InitButtonEvents();
        //Calling the Browser call for finding nearest pharmacy
    }

    private void InitButtonEvents() {
        //1) Getting reference to button
        BtnFindNearest = (Button) findViewById(R.id.BtnFindNearest);
        BtnFbClick=(ImageButton) findViewById(R.id.ImgBtnFb);
        //2) Set Listener to run code
        BtnFindNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BrowseMaps=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.in/maps/search/thulasi+pharmacy"));
                startActivity(BrowseMaps);
            }
        });
       BtnFbClick.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               Intent BrowseFb=new Intent(Intent.ACTION_VIEW,Uri.parse("fb://profile/488040821264615"));
               if(BrowseFb.resolveActivity(getPackageManager()) != null){
                   startActivity(BrowseFb);
               }
               else{
               Intent BrowseFbWeb=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/ThulasiPharmacy"));
                   startActivity(BrowseFbWeb);
               }

           }
       });
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
