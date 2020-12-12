package com.kairan.uidesign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kairan.uidesign.Utils.StringsUsed;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CheckInSuccess extends AppCompatActivity {
    ImageView backButton;
    TextView textView_checkIn_date;
    TextView textView_checkIn_time;
    TextView textView_current_location;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkinsuccess);
        getSupportActionBar().hide();
        
        Log.i(StringsUsed.TAG,"go to check in success");
        // get and set check in time and date
        textView_checkIn_date = findViewById(R.id.checkin_date);
        textView_checkIn_time = findViewById(R.id.checkin_time);

        String dateFormat = "dd MMM yyyy";
        String timeFormat = "hh:mm aa";
        SimpleDateFormat date = new SimpleDateFormat(dateFormat);
        SimpleDateFormat time = new SimpleDateFormat(timeFormat);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

        textView_checkIn_date.setText(date.format(new Date()));
        Log.i(StringsUsed.TAG,date.format(new Date()));
        Log.i(StringsUsed.TAG,String.valueOf(textView_checkIn_date.getText()));

        textView_checkIn_time.setText(time.format(new Date()));
        Log.i(StringsUsed.TAG,time.format(new Date()));
        Log.i(StringsUsed.TAG,String.valueOf(textView_checkIn_time.getText()));

        // get and set current location (intent from check in activity)
        textView_current_location = findViewById(R.id.textView_current_location_checkIn);
        String checkInLocation = getIntent().getStringExtra(SafeEntryCheckIn.GET_CHECK_IN_LOCATION_SCAN);
        Log.i(StringsUsed.TAG,checkInLocation);
        textView_current_location.setText(checkInLocation);
        Log.i(StringsUsed.TAG,"set text");

        //display date
//        Calendar calendar = Calendar.getInstance();
//
//        String formattedDate = calendar.get(Calendar.DAY_OF_MONTH) + " " + fillCalendarMonth(monthsMap).get(calendar.get(Calendar.MONTH)+1) + " " + calendar.get(Calendar.YEAR);
//        textView_checkIn_date.setText(formattedDate);
//
//
//        SimpleDateFormat formattedTime = new SimpleDateFormat("HH:mm");
//        Date date = new Date();
//        textView_checkIn_time.setText(formattedTime.format(date));


        // back to home button (2 ways)
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
