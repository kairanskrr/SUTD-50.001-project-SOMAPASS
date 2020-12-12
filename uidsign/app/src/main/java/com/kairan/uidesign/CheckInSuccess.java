package com.kairan.uidesign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kairan.uidesign.Utils.StringsUsed;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class CheckInSuccess extends AppCompatActivity {
    ImageView backButton;
    TextView textView_checkIn_date;
    TextView textView_checkIn_time;
    TextView textView_current_location;
    HashMap<Integer, String> monthsMap = new HashMap<>();


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
        mBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent(CheckInSuccess.this, MenuActivity.class);
                startActivity(goBack);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        backButton = findViewById(R.id.back_arrow_check_success);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent(CheckInSuccess.this, MenuActivity.class);
                startActivity(goBack);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });


//        Intent intent = getIntent();
//        String result = intent.getExtras().getString("Location To Check Into");
//        textView_current_location = findViewById(R.id.textView_current_location_checkIn);
//        textView_current_location.setText(result);

    }

    //for date time @zen
    public static HashMap<Integer, String> fillCalendarMonth(HashMap<Integer, String> map){
        map.put(1, "JAN");map.put(2, "FEB");map.put(3, "MAR");map.put(4, "APR");map.put(5, "MAY");map.put(6, "JUN");map.put(7, "JUL");map.put(8, "AUG");map.put(9, "SEP");map.put(10, "OCT");map.put(11, "NOV");map.put(12, "DEC");
        return map;
    }


}
