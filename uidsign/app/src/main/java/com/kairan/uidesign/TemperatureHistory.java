package com.kairan.uidesign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kairan.uidesign.Utils.HttpRequest;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature_history);
        getSupportActionBar().hide();
        final int REQUEST_CODE_QR_SCAN = 101;
        datetime1 = findViewById(R.id.datetime1);
        temp1 = findViewById(R.id.temp1);
        datetime2 = findViewById(R.id.datetime2);
        temp2 = findViewById(R.id.temp2);
        datetime3 = findViewById(R.id.datetime3);
        temp3 = findViewById(R.id.temp3);
        HttpReqTempHistory httpreqtemphistory = new HttpReqTempHistory();
        httpreqtemphistory.execute("latesttemperatures",
                ToSharePreferences.GetSharedPreferences(TemperatureHistory.this,"userid"),
                ToSharePreferences.GetSharedPreferences(TemperatureHistory.this,"password"));

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
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_scan:
                    startActivity(new Intent(getApplicationContext(),SafeEntryCheckIn.class));
                    startActivityForResult(new Intent(getApplicationContext(), QrCodeActivity.class), REQUEST_CODE_QR_SCAN);
                    overridePendingTransition(0,0);
                    return true;

                case R.id.navigation_home:
                    startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                    return true;

                case R.id.navigation_declare:
                    startActivity(new Intent(getApplicationContext(),TempTaking.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.navigation_profile:
                    return true;

            }
            return false;
        });
    }


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
                    datetime1.setText(jsonObject.getString("datetime1"));
                    temp1.setText(jsonObject.getString("temp1"));
                    datetime2.setText(jsonObject.getString("datetime2"));
                    temp2.setText(jsonObject.getString("temp2"));
                    datetime3.setText(jsonObject.getString("datetime3"));
                    temp3.setText(jsonObject.getString("temp3"));
                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }}
        }
    }

}