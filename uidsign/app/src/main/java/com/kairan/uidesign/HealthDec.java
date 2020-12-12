package com.kairan.uidesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_scan:
                    startActivityForResult(new Intent(getApplicationContext(), QrCodeActivity.class), MenuActivity.getRequestCodeQrScan());
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.navigation_home:
                    startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.navigation_declare:
                    return true;

                case R.id.navigation_profile:
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;

            }
            return false;
        });
        backbutton = findViewById(R.id.imageView_back_fromhealth);
        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(HealthDec.this,MenuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
                        (dialog, which) -> dialog.dismiss());
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
            Intent openConfirmation = new Intent(HealthDec.this, SafeEntryCheckIn.class);
            openConfirmation.putExtra("Location To Check Into", result);
            startActivity(openConfirmation);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);


        }
    }
}