package com.kairan.uidesign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
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

        Log.i(tag, "go to check in success");

        textView_checkIn_date = findViewById(R.id.checkin_date);
        textView_checkIn_time = findViewById(R.id.checkin_time);


        String dateFormat = "dd MMM yyyy";
        String timeFormat = "hh:mm aa";
        SimpleDateFormat date = new SimpleDateFormat(dateFormat);
        SimpleDateFormat time = new SimpleDateFormat(timeFormat);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

        textView_checkIn_date.setText(date.format(new Date()));
        Log.i(tag, date.format(new Date()));
        textView_checkIn_time.setText(time.format(new Date()));
        Log.i(tag, time.format(new Date()));
        Log.i(tag, String.valueOf(textView_checkIn_date.getText()));
        Log.i(tag, String.valueOf(textView_checkIn_time.getText()));

        Log.i(tag, "go to scan activity");
        textView_current_location = findViewById(R.id.textView_current_location_checkIn);

        String checkInLocation = getIntent().getStringExtra(SafeEntryCheckIn.GET_CHECK_IN_LOCATION_SCAN);
        Log.i(tag, checkInLocation);
        textView_current_location.setText(checkInLocation);
        Log.i(tag, "set text");

        Button mBackToHome = findViewById(R.id.backToHome);
        mBackToHome.setOnClickListener(v -> {
            Intent goBack = new Intent(CheckInSuccess.this, MenuActivity.class);
            startActivity(goBack);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });

        backButton = findViewById(R.id.back_arrow_check_success);
        backButton.setOnClickListener(v -> {
            Intent goBack = new Intent(CheckInSuccess.this, MenuActivity.class);
            startActivity(goBack);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });

    }
}
