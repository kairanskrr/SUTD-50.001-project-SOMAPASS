package com.kairan.uidesign;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kairan.uidesign.Utils.HttpRequest;
import com.kairan.uidesign.Utils.StringsUsed;
import com.kairan.uidesign.Utils.ToSharePreferences;

import org.json.JSONException;
import org.json.JSONObject;


public class MenuActivity extends AppCompatActivity{
    TextView textview_username_menu;
    ImageButton temp_imagebutton;
    ImageButton healthdec_imagebutton;
    ImageView small_QR_icon;
    TextView textView_search_menu;
    TextView latestCheckIn;
    TextView latestCheckInTime;
    Button checkout_home;
    public String tag = "SHARED";
    public static String checkIn_location_intent = "Location To Check Into";
    final int CHECK_IN_LOCATION_MENU = 1111;

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
//                        startActivity(new Intent(getApplicationContext(),SafeEntryCheckIn.class));
//                        overridePendingTransition(0,0);


                        startActivityForResult(new Intent(getApplicationContext(), QrCodeActivity.class), REQUEST_CODE_QR_SCAN);
                        Log.i(tag,"startActivity_QRSCAN");
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;

                    case R.id.navigation_home:
                        //startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                        //overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_declare:
                        startActivity(new Intent(getApplicationContext(),TempTaking.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;

                    case R.id.navigation_profile:
                        startActivity(new Intent(MenuActivity.this,ProfileActivity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;

                }
                return false;
            }
        });
        small_QR_icon = findViewById(R.id.imageView_qrcode_icon);
        small_QR_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), QrCodeActivity.class), REQUEST_CODE_QR_SCAN);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        });
        latestCheckIn = findViewById(R.id.textView_current_latest_checkin);
        latestCheckInTime = findViewById(R.id.textView_current_latest_checkintime);
        // get latest checkin and update latestCheckIn
        HttpGetRequest httpRequest_LatestCheckIn = new HttpGetRequest();
        httpRequest_LatestCheckIn.execute(StringsUsed.LatestCheckIn_http,
                ToSharePreferences.GetSharedPreferences(MenuActivity.this,StringsUsed.user_id_sp),
                ToSharePreferences.GetSharedPreferences(MenuActivity.this,StringsUsed.user_password_sp));
        //httpgetrequest below


        // go to temperature taking
        temp_imagebutton = findViewById(R.id.temp_imagebutton);
        temp_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,TempTaking.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        // go to health declaration
        healthdec_imagebutton = findViewById(R.id.healthdec_imagebutton);
        healthdec_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,HealthDec.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        // go to search
        textView_search_menu = findViewById(R.id.textView_search_menu);
        textView_search_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });


        /**
         * FOR NIC: functionality of check out button
         */
        checkout_home = findViewById(R.id.checkout_home);
        checkout_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INSTEAD OF IMMEDIATE CHECKOUT, GO TO NEW ACTIVITY FOR CHECKOUT CARD
//                MenuActivity.HttpGetRequestCheckout httpreqcheckout = new MenuActivity.HttpGetRequestCheckout();
//                httpreqcheckout.execute();
                Intent intent_to_checkout_card = new Intent(MenuActivity.this,SafeEntryCheckout.class);
                startActivity(intent_to_checkout_card);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

    }



    // HTTPGetRequest Class to handle login network logic

    /**
     * DELEGATION
     */

    class HttpGetRequest extends HttpRequest {

        @Override
        protected void onPostExecute(String result) {
            if (result == null){
                //Toast.makeText(MenuActivity.this,"Not checked in to anywhere yet.",Toast.LENGTH_LONG).show();
                latestCheckIn.setText("No Check Ins");
                latestCheckInTime.setText("");
                checkout_home.setVisibility(View.INVISIBLE);
            }
            else{
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    SharedPreferences sharedPreferences = getSharedPreferences(StringsUsed.pref_file_sp, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(StringsUsed.checkoutLocation_sp,jsonObject.getString(StringsUsed.locationName_json));
                    editor.commit();
                    //Toast.makeText(MenuActivity.this, result, Toast.LENGTH_SHORT).show();
                    latestCheckIn.setText(jsonObject.getString(StringsUsed.locationName_json));
                    latestCheckInTime.setText(jsonObject.getString(StringsUsed.checkInTimeReadable_json));
                    checkout_home.setVisibility(View.VISIBLE);
                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }}
        }
    }
    /*class HttpGetRequest extends AsyncTask<String, Void, String> {
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
//                setAuthority("latestcheckin");
//                URL myUrl = new URL(HttpRequest.makeURL(getAuthority()).toString());

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
                latestCheckIn.setText("No Check Ins");
                latestCheckInTime.setText("");
                checkout_home.setVisibility(View.INVISIBLE);
            }
            else{
                try {
                    jsonObject = new JSONObject(result);
                        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("checkoutlocationnamecard",jsonObject.getString("locationname"));
                        editor.commit();
                    //Toast.makeText(MenuActivity.this, result, Toast.LENGTH_SHORT).show();
                    latestCheckIn.setText(jsonObject.getString("locationname"));
                    latestCheckInTime.setText(jsonObject.getString("checkintimereadable"));
                    checkout_home.setVisibility(View.VISIBLE);


                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }}

        }

    }*/


    //end of latestCheckIn
    //start of checkout button

//    // HTTPGetRequest Class to check latest checked in location
//    class HttpGetRequestCheckout extends AsyncTask<String, Void, String> {
//        public static final String REQUEST_METHOD = "GET";
//        public static final int READ_TIMEOUT = 15000;
//        public static final int CONNECTION_TIMEOUT = 15000;
//
//        protected String doInBackground(String... params){
//            //String stringUrl = params[0];
//            String result;
//            String inputLine;
//            SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
//            String useridtosend = sharedPreferences.getString("userid","UNDEFINED");;
//            String passwordtosend = sharedPreferences.getString("password","UNDEFINED");;
//            try {
//                //Create a URL object holding our url
//                //TODO TO implement the URL Builder taught to us instead of string concat for URL
//                URL myUrl = new URL("https://somapass.xyz/latestcheckout/"+useridtosend+"/"+passwordtosend);
//                //Create a connection
//                HttpURLConnection connection =(HttpURLConnection)
//                        myUrl.openConnection();
//                //Set methods and timeouts
//                connection.setRequestMethod(REQUEST_METHOD);
//                connection.setReadTimeout(READ_TIMEOUT);
//                connection.setConnectTimeout(CONNECTION_TIMEOUT);
//
//                //Connect to our url
//                connection.connect();
//                //Create a new InputStreamReader
//                InputStreamReader streamReader = new
//                        InputStreamReader(connection.getInputStream());
//                //Create a new buffered reader and String Builder
//                BufferedReader reader = new BufferedReader(streamReader);
//                StringBuilder stringBuilder = new StringBuilder();
//                //Check if the line we are reading is not null
//                while((inputLine = reader.readLine()) != null){
//                    stringBuilder.append(inputLine);
//                }
//                //Close our InputStream and Buffered reader
//                reader.close();
//                streamReader.close();
//                //Set our result equal to our stringBuilder
//                result = stringBuilder.toString();
//            }
//            catch(IOException e){
//                e.printStackTrace();
//                result = null;
//            }
//            return result;
//        }
//        protected void onPostExecute(String result){
//            JSONObject jsonObject;
//            if (result == null){
//                Toast.makeText(MenuActivity.this,"NULL POST EXECUTE CHECKOUT.",Toast.LENGTH_LONG).show();
//            }
//            else{
//                Toast.makeText(MenuActivity.this,"Success Checking out.",Toast.LENGTH_LONG).show();
//                Intent intent2 = new Intent(MenuActivity.this,MainActivity.class);
//                startActivity(intent2);
//            }
//
//        }
//    }
//
//
//    //end of latestCheckIn
//
//    //end of checkout button


    /*****************
     * QR Code Scanner with ASyncTask to open next activity that showcases check in successful
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(tag,"onActivityResult");
        Log.i(tag,"data: "+data);
        Log.i(tag,"resultCode: "+requestCode);
        if (resultCode != Activity.RESULT_OK) {
            Log.d(LOGTAG, "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {

                /*Log.i(tag,"result: "+result);
                Log.i(tag,"resultCode != Activity.RESULT_OK");
                SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
                Log.i(tag,sharedPreferences.toString());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.i(tag,editor.toString());
                editor.putString("checkinlocationnamecard",result);
                Log.i(tag,"editor, put check in location");

                editor.commit();
                Log.i(tag,"editor commit");*/


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

            //Log.i(StringsUsed.TAG,"requestCode == REQUEST_CODE_QR_CODE");
            //Log.i(StringsUsed.TAG,result);
            SharedPreferences sharedPreferences = getSharedPreferences(StringsUsed.pref_file_sp, Context.MODE_PRIVATE);
            //Log.i(StringsUsed.TAG,sharedPreferences.toString());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //Log.i(StringsUsed.TAG,editor.toString());
            editor.putString(StringsUsed.checkInLocation_sp,result);
            editor.commit();
            //Log.i(StringsUsed.TAG,"editor, put check in location");

            /*
            code that check in straightaway without confirmation:

            executeCheckIn openQR = new executeCheckIn();
            openQR.execute(result);

            Intent successScreen = new Intent(MenuActivity.this, SafeEntryCheckIn.class);
            startActivity(successScreen);
             */


            /*
            code that ask for confirmation with a check in button
             */


            Intent openConfirmation = new Intent(MenuActivity.this, SafeEntryCheckIn.class);
            openConfirmation.putExtra(checkIn_location_intent, result);
            startActivityForResult(openConfirmation,CHECK_IN_LOCATION_MENU);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
            //startActivity(openConfirmation);


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

