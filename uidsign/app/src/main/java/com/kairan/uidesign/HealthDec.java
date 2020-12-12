package com.kairan.uidesign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HealthDec extends AppCompatActivity {

    ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthdec);
        getSupportActionBar().hide();

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);

        //Set Dec Selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_scan:
                        startActivityForResult(new Intent(getApplicationContext(), QrCodeActivity.class), MenuActivity.getRequestCodeQrScan());
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;

                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;

                    case R.id.navigation_declare:
                        return true;

                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;
                }
                return false;
            }
        });

        // button back to home
        backbutton = findViewById(R.id.imageView_back_fromhealth);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthDec.this,MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
                AlertDialog alertDialog = new AlertDialog.Builder(HealthDec.this).create();
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
            Intent openConfirmation = new Intent(HealthDec.this, SafeEntryCheckIn.class);
            openConfirmation.putExtra("Location To Check Into", result);
            startActivity(openConfirmation);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);


        }
    }
}