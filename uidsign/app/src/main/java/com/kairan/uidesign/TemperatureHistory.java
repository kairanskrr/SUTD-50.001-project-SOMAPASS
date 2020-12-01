package com.kairan.uidesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TemperatureHistory extends AppCompatActivity {

    TextView datetime1;
    TextView temp1;
    TextView datetime2;
    TextView temp2;
    TextView datetime3;
    TextView temp3;
    ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature_history);
        getSupportActionBar().hide();
        final int REQUEST_CODE_QR_SCAN = 101;
        datetime1 = findViewById(R.id.datetime1);
        temp1 = findViewById(R.id.temp1);
        datetime2 = findViewById(R.id.datetime2);
        temp2 = findViewById(R.id.temp2);
        datetime3 = findViewById(R.id.datetime3);
        temp3 = findViewById(R.id.temp3);
        TemperatureHistory.HttpGetRequestTempHistory httpreqtemphistory = new TemperatureHistory.HttpGetRequestTempHistory();
        httpreqtemphistory.execute();

        backbutton = findViewById(R.id.imageView_back_fromtemphist);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TemperatureHistory.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_scan:
//                        startActivity(new Intent(getApplicationContext(),ScanActivity.class));
//                        overridePendingTransition(0,0);


                        startActivityForResult(new Intent(getApplicationContext(), QrCodeActivity.class), REQUEST_CODE_QR_SCAN);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_home:
                        //startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                        //overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_declare:
                        startActivity(new Intent(getApplicationContext(),TempTaking.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_profile:
                        startActivity(new Intent(TemperatureHistory.this,ProfileActivity.class));
                        //overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }


    public class HttpGetRequestTempHistory extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        protected String doInBackground(String... params){
            //String stringUrl = params[0];
            String result;
            String inputLine;
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
            String useridtosend = sharedPreferences.getString("userid","UNDEFINED");;
            String passwordtosend = sharedPreferences.getString("password","UNDEFINED");;
            try {
                //Create a URL object holding our url
                //TODO TO implement the URL Builder taught to us instead of string concat for URL
//                URL myUrl = new URL("https://somapass.xyz/latesttemperatures/"+useridtosend+"/"+passwordtosend);
                URL myUrl = new URL("https://somapass.xyz/latesttemperatures/1/1");
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            JSONObject jsonObject;
            if (result == null){
                Toast.makeText(TemperatureHistory.this,"No Temperature declarations.",Toast.LENGTH_LONG).show();


            }
            else{
                try {
                    jsonObject = new JSONObject(result);
//                        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("userid",jsonObject.getString("userid"));
//                        editor.putString("password",jsonObject.getString("password"));
//                        editor.putString("name",jsonObject.getString("name"));
//                        editor.commit();
                    //Toast.makeText(MenuActivity.this, result, Toast.LENGTH_SHORT).show();
//                    latestCheckIn.setText(jsonObject.getString("locationname"));
//                    latestCheckInTime.setText(jsonObject.getString("checkintimereadable"));

                    datetime1.setText(jsonObject.getString("datetime1"));
                    temp1.setText(jsonObject.getString("temp1"));
                    datetime2.setText(jsonObject.getString("datetime2"));
                    temp2.setText(jsonObject.getString("temp2"));
                    datetime3.setText(jsonObject.getString("datetime3"));
                    temp3.setText(jsonObject.getString("temp3"));
                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }}

        }
    }

}