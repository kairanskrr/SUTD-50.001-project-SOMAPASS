package com.kairan.uidesign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CheckInSuccess extends AppCompatActivity {
    ImageView backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkinsuccess);
        getSupportActionBar().hide();


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
