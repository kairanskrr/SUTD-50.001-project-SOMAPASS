package com.kairan.uidesign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    TextView logout_button;
    ImageView backbutton;
    TextView temphistory;
    TextView userName;
    TextView studentId;
    public String tag = "SHARED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        userName = findViewById(R.id.textView_username_profile_right);
        studentId = findViewById(R.id.textView_studentid_profile_right);

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
        String loggedInName = sharedPreferences.getString("name","UNDEFINED");
        String loggedInId = sharedPreferences.getString("userid","UNDEFINED");
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
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_declare:
                        startActivity(new Intent(getApplicationContext(), TempTaking.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_profile:
                        //startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
            }
        });

        // Implement Log out Button
        logout_button = findViewById(R.id.textView_signout_profile);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userid", null);
                editor.putString("password", null);
                editor.putString("name", null);
                editor.commit();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);

            }
        });

        backbutton = findViewById(R.id.imageView_back_profile);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
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
                String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
                if (result != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("checkinlocationnamecard",String.valueOf(data));

                    editor.commit();

                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
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
            if (requestCode == MenuActivity.getRequestCodeQrScan()) {
                if (data == null)
                    return;
                //Getting the passed result
                String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Log.d(MenuActivity.getLOGTAG(), "Have scan result in your app activity :" + result);

                Log.i(tag,"requestCode == REQUEST_CODE_QR_CODE");
                Log.i(tag,result);
                SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
                Log.i(tag,sharedPreferences.toString());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.i(tag,editor.toString());
                editor.putString("checkInLocation",result);
                editor.commit();
                Log.i(tag,"editor, put check in location");

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
                Intent openConfirmation = new Intent(ProfileActivity.this, SafeEntryCheckIn.class);
                openConfirmation.putExtra("Location To Check Into", result);
                startActivity(openConfirmation);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);

            }
        }
}
