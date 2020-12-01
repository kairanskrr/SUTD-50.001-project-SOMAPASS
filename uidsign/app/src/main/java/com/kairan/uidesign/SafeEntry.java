package com.kairan.uidesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class SafeEntry extends AppCompatActivity {
    TextView locationanamecard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_entry);
        locationanamecard = findViewById(R.id.textView_current_location);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
        String checkinlocationnamecard = sharedPreferences.getString("checkinlocationnamecard","UNDEFINED");;
        System.out.println("==========================================");
        System.out.println(checkinlocationnamecard);
        locationanamecard.setText("HELLO");

    }
}