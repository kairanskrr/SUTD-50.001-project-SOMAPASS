package com.kairan.uidesign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kairan.uidesign.Utils.HttpRequest;
import com.kairan.uidesign.Utils.StringsUsed;
import com.kairan.uidesign.Utils.ToSharePreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class TemperatureHistory extends AppCompatActivity {
    TextView datetime1;
    TextView temp1;
    TextView datetime2;
    TextView temp2;
    TextView datetime3;
    TextView temp3;
    ImageButton backbutton;
    final int REQUEST_CODE_QR_SCAN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature_history);
        getSupportActionBar().hide();

        // set text: last three temperature taking
        datetime1 = findViewById(R.id.datetime1);
        temp1 = findViewById(R.id.temp1);
        datetime2 = findViewById(R.id.datetime2);
        temp2 = findViewById(R.id.temp2);
        datetime3 = findViewById(R.id.datetime3);
        temp3 = findViewById(R.id.temp3);
        HttpReqTempHistory httpreqtemphistory = new HttpReqTempHistory();
        httpreqtemphistory.execute(StringsUsed.LatestTempHistory_http,
                ToSharePreferences.GetSharedPreferences(TemperatureHistory.this,StringsUsed.user_id_sp),
                ToSharePreferences.GetSharedPreferences(TemperatureHistory.this,StringsUsed.user_password_sp));

        // back to profile activity
        backbutton = findViewById(R.id.imageView_back_fromtemphist);
        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(TemperatureHistory.this,ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
        });

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_scan:
                        startActivityForResult(new Intent(getApplicationContext(), QrCodeActivity.class), REQUEST_CODE_QR_SCAN);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;

                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;

                    case R.id.navigation_declare:
                        startActivity(new Intent(getApplicationContext(),TempTaking.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        return true;

                    case R.id.navigation_profile:
                        return true;
                }
                return false;
            }
        });
    }


    // http request to get temperature history
    class HttpReqTempHistory extends HttpRequest {

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject;
            if (result == null){
                Toast.makeText(TemperatureHistory.this,"No Temperature declarations.",Toast.LENGTH_LONG).show();
                datetime1.setText("Please do your temperature declarations :)");
            }
            else{
                try {
                    jsonObject = new JSONObject(result);
                    datetime1.setText(jsonObject.getString(StringsUsed.tempTaking_dateTime1));
                    temp1.setText(jsonObject.getString(StringsUsed.tempTaking_temp1));
                    datetime2.setText(jsonObject.getString(StringsUsed.tempTaking_dateTime2));
                    temp2.setText(jsonObject.getString(StringsUsed.tempTaking_temp2));
                    datetime3.setText(jsonObject.getString(StringsUsed.tempTaking_dateTime3));
                    temp3.setText(jsonObject.getString(StringsUsed.tempTaking_temp3));
                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }}
        }
    }

}