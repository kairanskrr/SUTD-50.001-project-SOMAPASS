package com.kairan.uidesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.kairan.uidesign.Utils.StringsUsed;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Check shared preferences if user is logged in.
        SharedPreferences sharedPreferences = getSharedPreferences(StringsUsed.pref_file_sp, Context.MODE_PRIVATE);
        String loggedInUserID = sharedPreferences.getString(StringsUsed.user_id_sp,null);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (loggedInUserID != null){
                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                        }else{
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                        }
                    }
                },
                2000);





//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(intent);
    }
}