package com.kairan.uidesign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class MenuActivity extends AppCompatActivity {
    TextView textview_username_menu;
    ImageButton temp_imagebutton;
    ImageButton healthdec_imagebutton;
    TextView latestCheckIn;
    TextView latestCheckInTime;

    /**
     * FOR NIC: use getter and setter to set scanActivityState during "onClick" of the check out button you implemented. This sets the visibiltiy of checkin to non-visible and checkout visible
     * Set to 1 to make the button "Check Out"
     * Set to 0 AFTER onClick on check out button(to set the button back to Check In)
     */
    Button mCheckOutHome;


    //Track scanactivity state
    private static int scanActivityState = 0;

    public static int getScanActivityState() {
        return scanActivityState;
    }
    public static void setScanActivityState(int scanActivityState) {
        MenuActivity.scanActivityState = scanActivityState;
    }

    //Request code for QR code scan
    private static final String LOGTAG = "Scan for entry";
    private static final int REQUEST_CODE_CAMERA = 100;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 102;

    public static int getRequestCodeQrScan() {
        return REQUEST_CODE_QR_SCAN;
    }

    public static String getLOGTAG() {
        return LOGTAG;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
        textview_username_menu = findViewById(R.id.textview_username_menu);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
        String loggedInName = sharedPreferences.getString("name","UNDEFINED");
        textview_username_menu.setText(loggedInName);

        //get user permission
        checkPermission(Manifest.permission.CAMERA, REQUEST_CODE_CAMERA);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_EXTERNAL_STORAGE);


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
                        startActivity(new Intent(MenuActivity.this,ProfileActivity.class));
                        //overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        latestCheckIn = findViewById(R.id.textView_current_latest_checkin);
        latestCheckInTime = findViewById(R.id.textView_current_latest_checkintime);
        // get latest checkin and update latestCheckIn
        MenuActivity.HttpGetRequest httpreq = new MenuActivity.HttpGetRequest();
        httpreq.execute();
        //httpgetrequest below


        temp_imagebutton = findViewById(R.id.temp_imagebutton);
        temp_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,TempTaking.class);
                startActivity(intent);
            }
        });
        healthdec_imagebutton = findViewById(R.id.healthdec_imagebutton);
        healthdec_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,HealthDec.class);
                startActivity(intent);
            }
        });


        /**
         * FOR NIC: functionality of check out button
         */
        mCheckOutHome = findViewById(R.id.checkout_home);
        mCheckOutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    // HTTPGetRequest Class to handle login network logic
    class HttpGetRequest extends AsyncTask<String, Void, String> {
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
                URL myUrl = new URL("https://somapass.xyz/latestcheckin/"+useridtosend+"/"+passwordtosend);
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
                Toast.makeText(MenuActivity.this,"Not checked in to anywhere yet.",Toast.LENGTH_LONG).show();
                latestCheckIn.setText("No Check Ins");
                latestCheckInTime.setText("");
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
                    latestCheckIn.setText(jsonObject.getString("locationname"));
                    latestCheckInTime.setText(jsonObject.getString("checkintimereadable"));



                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }}

        }
    }


    //end of latestCheckIn


    /*****************
     * QR Code Scanner with ASyncTask to open next activity that showcases check in successful
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.d(LOGTAG, "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(MenuActivity.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d(LOGTAG, "Have scan result in your app activity :" + result);

            /*
            code that check in straightaway without confirmation:

            executeCheckIn openQR = new executeCheckIn();
            openQR.execute(result);

            Intent successScreen = new Intent(MenuActivity.this, ScanActivity.class);
            startActivity(successScreen);
             */


            /*
            code that ask for confirmation with a check in button
             */
            Intent openConfirmation = new Intent(MenuActivity.this, ScanActivity.class);
            openConfirmation.putExtra("Location To Check Into", result);
            startActivity(openConfirmation);


        }
    }
    // Function to check and request permission
    private void checkPermission(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(MenuActivity.this,
                permission) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat
                    .requestPermissions(
                            MenuActivity.this,
                            new String[] { permission },
                            requestCode);
        }
    }



    /*
    // HTTPGetRequest Class to handle login network logic
    class executeCheckIn extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        protected String doInBackground(String... urls){
            //String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(urls[0]);
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
    }
    */
}
