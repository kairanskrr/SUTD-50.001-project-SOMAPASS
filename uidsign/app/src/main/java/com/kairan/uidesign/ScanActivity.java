package com.kairan.uidesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// maybe rename this activity?
public class ScanActivity extends AppCompatActivity {
    ImageView backButton;
    TextView checkinlocationnamecard;
    public String tag = "SHARED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_entry);
        getSupportActionBar().hide();

        Log.i(tag,"go to scan activity");
        checkinlocationnamecard = findViewById(R.id.textView_current_location);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
        Log.i(tag,sharedPreferences.toString());
        String checkinlocationnamecardstring = sharedPreferences.getString("checkInLocation","UNDEFINED");
        Log.i(tag,checkinlocationnamecardstring);
        checkinlocationnamecard.setText(checkinlocationnamecardstring);
        Log.i(tag,"set text");

        //check in and out buttons
        Button mCheckIn = findViewById(R.id.button_checkIn_safeEntry);
        //Button mCheckOut = findViewById(R.id.button_checkOut_safeEntry);


        //button functions
        mCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                String result = intent.getExtras().getString("Location To Check Into");
                // System.out.println("++++++++++++++++++++++++");
                // System.out.println(result);

                /*SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("checkinlocationnamecard",result);
                editor.commit();*/

                // checkinlocationnamecard = findViewById(R.id.textView_current_location);
                // String checkinlocationnamecardstring = sharedPreferences.getString("checkinlocationnamecard","UNDEFINED");;


                executeCheckIn openQR = new executeCheckIn();
                openQR.execute(result);

                Intent successScreen = new Intent(ScanActivity.this, CheckInSuccess.class);
                startActivity(successScreen);

            }
        });
        /**
         * FOR NICHOLAS TO IMPLEMENT
         */
        /*mCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: make checkout button check out in database
            }
        });

        if(MenuActivity.getScanActivityState() == 1){
            checkinlocationnamecard.setText(checkinlocationnamecardstring);
            mCheckIn.setVisibility(View.GONE);
            mCheckOut.setVisibility(View.VISIBLE);
        }else{
            checkinlocationnamecard.setText(checkinlocationnamecardstring);
            mCheckIn.setVisibility(View.VISIBLE);
            mCheckOut.setVisibility(View.GONE );
        }*/


        backButton = findViewById(R.id.back_from_safeentry);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mgoBack = new Intent(ScanActivity.this, MenuActivity.class);
                startActivity(mgoBack);
            }
        });




    }
    // HTTPGetRequest Class to handle login network logic
    class executeCheckIn extends AsyncTask<String, String, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;


        protected String doInBackground(String... urls){
            //String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
                String useridtosend = sharedPreferences.getString("userid","UNDEFINED");;
                String passwordtosend = sharedPreferences.getString("password","UNDEFINED");;
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("checkinlocationnamecard",urls[0]);
//
//                editor.commit();

                publishProgress(String.valueOf(urls[0]));


                URL myUrl = new URL("https://somapass.xyz/checkin/"+useridtosend+"/"+passwordtosend+"/1/"+urls[0]);
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
                Toast.makeText(ScanActivity.this,"NULL POST EXECUTE CHECKOUT.",Toast.LENGTH_LONG).show();
            }
            else{
                checkinlocationnamecard.setText(result);
                Toast.makeText(ScanActivity.this,"Success Checking In.",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            checkinlocationnamecard.setText(values[0]);
        }
    }








    // HTTPGetRequest Class to check latest checked in location
    class HttpGetRequestCheckout extends AsyncTask<String, Void, String> {
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

            // TODO: 2/12/2020 make locationname be fetched from QR code instead of entire link
            String locationname = "DSL";
            try {
                //Create a URL object holding our url
                //TODO TO implement the URL Builder taught to us instead of string concat for URL
                URL myUrl = new URL("https://somapass.xyz/checkin/"+useridtosend+"/"+passwordtosend+"/1/"+locationname);
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
                Toast.makeText(ScanActivity.this,"NULL POST EXECUTE CHECKOUT.",Toast.LENGTH_LONG).show();
            }
            else{
                checkinlocationnamecard.setText(result);
                Toast.makeText(ScanActivity.this,"Success Checking out.",Toast.LENGTH_LONG).show();
                //Intent intent2 = new Intent(ScanActivity.this,MainActivity.class);
                //startActivity(intent2);
            }

        }
    }



}