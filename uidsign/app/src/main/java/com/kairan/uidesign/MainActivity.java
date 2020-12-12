package com.kairan.uidesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Check shared preferences if user is logged in.
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
        String loggedInUserID = sharedPreferences.getString("userid",null);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (loggedInUserID != null){
                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_out, R.anim.fade_out);
                        }else{
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_out, R.anim.fade_out);
                        }
                    }
                },
                2000);





//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(intent);
    }
}