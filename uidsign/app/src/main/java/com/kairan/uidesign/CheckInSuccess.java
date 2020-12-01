package com.kairan.uidesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class CheckInSuccess extends AppCompatActivity {
    ImageView backButton;
    TextView textView_checkIn_date;
    TextView textView_checkIn_time;
    TextView textView_current_location;
    public String tag = "SHARED";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkinsuccess);
        getSupportActionBar().hide();

        textView_checkIn_date = findViewById(R.id.checkin_date);
        textView_checkIn_time = findViewById(R.id.checkin_time);


        String dateFormat = "dd MMM yyyy";
        String timeFormat = "hh:mm aa";
        Date date = new Date(dateFormat);
        Date time = new Date(timeFormat);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        textView_checkIn_date.setText(String.valueOf(date.getTime()));
        textView_checkIn_time.setText(String.valueOf(time.getTime()));

        Log.i(tag,"go to scan activity");
        textView_current_location = findViewById(R.id.textView_current_location_checkIn);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
        Log.i(tag,sharedPreferences.toString());
        String checkInLocation = sharedPreferences.getString("checkInLocation","UNDEFINED");
        Log.i(tag,checkInLocation);
        textView_current_location.setText(checkInLocation);
        Log.i(tag,"set text");



        Button mBackToHome = findViewById(R.id.backToHome);
        mBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent(CheckInSuccess.this, MenuActivity.class);
                startActivity(goBack);
            }
        });

        backButton = findViewById(R.id.back_arrow_check_success);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent(CheckInSuccess.this, MenuActivity.class);
                startActivity(goBack);
            }
        });
    }
}
