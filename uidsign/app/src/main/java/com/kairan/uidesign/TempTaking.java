package com.kairan.uidesign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kairan.uidesign.Utils.HttpRequest;
import com.kairan.uidesign.Utils.StringsUsed;
import com.kairan.uidesign.Utils.ToSharePreferences;

public class TempTaking extends AppCompatActivity {
    ImageButton backbutton;
    Button temp_submit;
    Button temp_history_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temptaking);
        getSupportActionBar().hide();

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);

        //Set Dec Selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_declare);

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

        // back to home
        backbutton = findViewById(R.id.imageView_back_fromtemp);
        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(TempTaking.this,MenuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
        });

        //Submit button
        temp_submit = findViewById(R.id.temp_submit);
        temp_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpReqNewTemp newTempReq = new HttpReqNewTemp();
                newTempReq.execute(StringsUsed.NewTemp_http,
                        ToSharePreferences.GetSharedPreferences(TempTaking.this,StringsUsed.user_id_sp),
                        ToSharePreferences.GetSharedPreferences(TempTaking.this,StringsUsed.user_password_sp),
                        currentTemperature);
            }
        });

        temp_history_button = findViewById(R.id.temp_history_button);
        temp_history_button.setOnClickListener(v -> {
            Intent intent_to_temp_history = new Intent(TempTaking.this,TemperatureHistory.class);
            startActivity(intent_to_temp_history);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
        });

    }


    public String currentTemperature = null;
    public void lessthan376clicked(View view) {
        currentTemperature="Less than 37.6";
    }

    public void morethan377wellclicked(View view) {
        currentTemperature="More than 37.6 but well";
    }

    public void morethan377notwell(View view) {
        currentTemperature="More than 37.6 unwell";
    }

    // HTTPGetRequest Class to check latest checked in location
    class HttpReqNewTemp extends HttpRequest {

        @Override
        protected void onPostExecute(String result) {
            if (result == null){
                Toast.makeText(TempTaking.this,"There was an error with Temperature Declaration.",Toast.LENGTH_LONG).show();
            }
            else{
                Intent intent = new Intent(TempTaking.this,MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
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

                AlertDialog alertDialog = new AlertDialog.Builder(TempTaking.this).create();
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
            Log.i(StringsUsed.TAG,"editor, put check in location");
            Intent openConfirmation = new Intent(TempTaking.this, SafeEntryCheckIn.class);
            openConfirmation.putExtra("Location To Check Into", result);
            startActivity(openConfirmation);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        }
    }

}