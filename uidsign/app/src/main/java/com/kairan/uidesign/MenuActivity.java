package com.kairan.uidesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {
    TextView textview_username_menu;
    ImageButton temp_imagebutton;
    ImageButton healthdec_imagebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        textview_username_menu = findViewById(R.id.textview_username_menu);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
        String loggedInName = sharedPreferences.getString("name","UNDEFINED");
        textview_username_menu.setText(loggedInName);

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_scan:
                        startActivity(new Intent(getApplicationContext(),ScanActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_home:
                        //startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                        //overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_declare:
                        startActivity(new Intent(getApplicationContext(),TempTaking.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_profile:
                        startActivity(new Intent(MenuActivity.this,ProfileActivity.class));
                        //overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        temp_imagebutton = findViewById(R.id.temp_imagebutton);
        temp_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,TempTaking.class);
                startActivity(intent);
            }
        });
        healthdec_imagebutton = findViewById(R.id.healthdec_imagebutton);
        healthdec_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,HealthDec.class);
                startActivity(intent);
            }
        });

    }
}