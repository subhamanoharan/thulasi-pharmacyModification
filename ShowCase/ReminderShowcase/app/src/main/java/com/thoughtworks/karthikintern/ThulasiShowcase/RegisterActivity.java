package com.thoughtworks.karthikintern.ThulasiShowcase;

/**
 * Created by karthikintern on 9/3/14.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.util.List;
public class RegisterActivity extends Activity {
    EditText email,password,phno;
    Button login,register;
    String emailtxt,passwordtxt,phnotxt;
    List<NameValuePair> params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        register = (Button)findViewById(R.id.registerbtn);
        phno=(EditText)findViewById(R.id.phno);
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regactivity = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(regactivity);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailtxt = email.getText().toString();
                passwordtxt = password.getText().toString();
                phnotxt=phno.getText().toString();
                //params = new ArrayList<NameValuePair>();
                //params.add(new BasicNameValuePair("email", emailtxt));
                //params.add(new BasicNameValuePair("password", passwordtxt));
                //params.add(new BasicNameValuePair("phno",phnotxt));
                //ServerRequest sr = new ServerRequest();
                //JSONObject json = sr.getJSON("http://10.0.2.2:8082/register",params);
                //JSONObject json = sr.getJSON("http://192.168.56.1:8080/register",params);
               // if(json != null){
                    //try{
                        String jsonstr = "Registered Successfully";
                        Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();
                        Log.d("Hello", jsonstr);
                    }//catch (JSONException e) {
                        //e.printStackTrace();
                   // }
            //    }
               /* Intent switchactivity=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(switchactivity);
                finish();*/
            //}
        });
    }
}
