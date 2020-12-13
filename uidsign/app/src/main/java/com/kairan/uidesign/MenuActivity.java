package com.kairan.uidesign;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
    private static final String LOGTAG = "Scan for entry";
    private static final int REQUEST_CODE_CAMERA = 100;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
        textview_username_menu = findViewById(R.id.textview_username_menu);
        SharedPreferences sharedPreferences = getSharedPreferences(StringsUsed.pref_file_sp, Context.MODE_PRIVATE);
        String loggedInName = sharedPreferences.getString(StringsUsed.user_name_sp,StringsUsed.undefined_sp);
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
                        startActivityForResult(new Intent(getApplicationContext(), QrCodeActivity.class), REQUEST_CODE_QR_SCAN);
                        Log.i(tag,"startActivity_QRSCAN");
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;

                    case R.id.navigation_home:
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
        small_QR_icon.setOnClickListener(v -> {
            startActivityForResult(new Intent(getApplicationContext(), QrCodeActivity.class), REQUEST_CODE_QR_SCAN);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
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

    /*****************
     * QR Code Scanner with ASyncTask to open next activity that showcases check in successful
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(StringsUsed.TAG,"onActivityResult");
        Log.i(StringsUsed.TAG,"data: "+data);
        Log.i(StringsUsed.TAG,"resultCode: "+requestCode);

        if (resultCode != Activity.RESULT_OK) {
            Log.d(LOGTAG, "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra(StringsUsed.requestError_scan);
            if (result != null) {

                AlertDialog alertDialog = new AlertDialog.Builder(MenuActivity.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra(StringsUsed.requestOk_scan);
            Log.d(LOGTAG, "Have scan result in your app activity :" + result);

            Log.i(StringsUsed.TAG,"requestCode == REQUEST_CODE_QR_CODE");
            Log.i(StringsUsed.TAG,result);
            SharedPreferences sharedPreferences = getSharedPreferences(StringsUsed.pref_file_sp, Context.MODE_PRIVATE);
            Log.i(StringsUsed.TAG,sharedPreferences.toString());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.i(StringsUsed.TAG,editor.toString());
            editor.putString(StringsUsed.checkInLocation_sp,result);
            editor.commit();
            Log.i(StringsUsed.TAG,"editor, put check in location");


            Intent openConfirmation = new Intent(MenuActivity.this, SafeEntryCheckIn.class);
            openConfirmation.putExtra(checkIn_location_intent, result);
            startActivityForResult(openConfirmation,CHECK_IN_LOCATION_MENU);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);


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

    public static int getRequestCodeQrScan() {
        return REQUEST_CODE_QR_SCAN;
    }

    public static String getLOGTAG() {
        return LOGTAG;
    }


}

