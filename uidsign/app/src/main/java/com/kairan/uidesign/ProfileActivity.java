package com.kairan.uidesign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kairan.uidesign.Utils.StringsUsed;

public class ProfileActivity extends AppCompatActivity {
    TextView logout_button;
    ImageView backbutton;
    TextView temphistory;
    TextView userName;
    TextView studentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        // set text: user name and id
        userName = findViewById(R.id.textView_username_profile_right);
        studentId = findViewById(R.id.textView_studentid_profile_right);
        SharedPreferences sharedPreferences = getSharedPreferences(StringsUsed.pref_file_sp, Context.MODE_PRIVATE);
        String loggedInName = sharedPreferences.getString(StringsUsed.user_name_sp,StringsUsed.undefined_sp);
        String loggedInId = sharedPreferences.getString(StringsUsed.user_id_sp,StringsUsed.undefined_sp);
        userName.setText(loggedInName);
        studentId.setText(loggedInId);

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);

        //Set Profile Selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_scan:
                        startActivityForResult(new Intent(getApplicationContext(), QrCodeActivity.class), MenuActivity.getRequestCodeQrScan());
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;

                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;

                    case R.id.navigation_declare:
                        startActivity(new Intent(getApplicationContext(), TempTaking.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;

                    case R.id.navigation_profile:
                        return true;
                }
                return false;
            }
        });

        //View TEMP HISTORY BUTTON
        temphistory = findViewById(R.id.textView_history_profile);
        temphistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttemphistory = new Intent(ProfileActivity.this,TemperatureHistory.class);
                startActivity(intenttemphistory);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        logout_button = findViewById(R.id.textView_signout_profile);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(StringsUsed.user_id_sp, null);
                editor.putString(StringsUsed.user_password_sp, null);
                editor.putString(StringsUsed.user_name_sp, null);
                editor.commit();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        }});

        // back to home button
        backbutton = findViewById(R.id.imageView_back_profile);
        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this,MenuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
        });
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
            if (resultCode != Activity.RESULT_OK) {
                Log.d(MenuActivity.getLOGTAG(), "COULD NOT GET A GOOD RESULT.");
                if (data == null)
                    return;
                //Getting the passed result
                String result = data.getStringExtra(StringsUsed.requestError_scan);
                if (result != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences(StringsUsed.pref_file_sp, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(StringsUsed.checkInLocationNameCard_sp,String.valueOf(data));

                    editor.commit();

                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                    alertDialog.setTitle("Scan Error");
                    alertDialog.setMessage("QR Code could not be scanned");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> dialog.dismiss());
                    alertDialog.show();
                }
                return;

            }
            if (requestCode == MenuActivity.getRequestCodeQrScan()) {
                if (data == null)
                    return;
                //Getting the passed result
                String result = data.getStringExtra(StringsUsed.requestOk_scan);
                Log.d(MenuActivity.getLOGTAG(), "Have scan result in your app activity :" + result);
                SharedPreferences sharedPreferences = getSharedPreferences(StringsUsed.pref_file_sp, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(StringsUsed.checkInLocation_sp,result);
                editor.commit();

                Intent openConfirmation = new Intent(ProfileActivity.this, SafeEntryCheckIn.class);
                openConfirmation.putExtra(StringsUsed.locationToCheckInto_scan, result);
                startActivity(openConfirmation);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);

            }
        }
}
