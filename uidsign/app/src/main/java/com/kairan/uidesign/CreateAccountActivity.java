package com.kairan.uidesign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kairan.uidesign.Utils.HttpRequest;

public class CreateAccountActivity extends AppCompatActivity {
    ImageView back_to_login;
    Button create_account_button;
    EditText student_id;
    EditText password;
    EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_account);
        back_to_login = findViewById(R.id.back_to_login);
        back_to_login.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAccountActivity.this,LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
        });
        student_id = findViewById(R.id.editText_name_create_account);
        password = findViewById(R.id.editText_password_create_account);
        name = findViewById(R.id.editText_studentname_create_account);
        //Button to Create Account Here
        create_account_button = findViewById(R.id.create_account_button);
        create_account_button.setOnClickListener(v -> {
            HttpReqCreateAccount httpreqcreate = new HttpReqCreateAccount();
            httpreqcreate.execute("createaccount",student_id.getText().toString(),password.getText().toString(),name.getText().toString());
        });

    }
    class HttpReqCreateAccount extends HttpRequest {
        @Override
        protected void onPostExecute(String result) {
            if (result == null){
                Toast.makeText(CreateAccountActivity.this,"Error, Please fill in both Student ID and Password or check network",Toast.LENGTH_LONG).show();}
            else{
                try {
                    Intent intent_create_success = new Intent(CreateAccountActivity.this,LoginActivity.class);
                    startActivity(intent_create_success);
                }catch (Exception err){
                    Log.d("Error", err.toString());
                }}
        }
    }

}