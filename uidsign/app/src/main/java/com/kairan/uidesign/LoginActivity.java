package com.kairan.uidesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kairan.uidesign.Utils.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText editText_username;
    EditText editText_passward;
    CheckBox checkBox_rememberME;
    Button button_login;
    Button button_create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);

        editText_username = findViewById(R.id.editText_username);
        editText_passward = findViewById(R.id.editText_password);
        checkBox_rememberME = findViewById(R.id.checkBox_rememberMe);
        button_login = findViewById(R.id.button_login);
        button_create_account = findViewById(R.id.button_create_account);

        //Click create account button: to createAccount
        button_create_account.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this,CreateAccountActivity.class);
            startActivity(intent);
        });
        button_login.setOnClickListener(v -> {
            HttpLogin httpreq = new HttpLogin();
            httpreq.execute("login",editText_username.getText().toString(),editText_passward.getText().toString());
        });
    }


    // HTTPGetRequest Class to handle login network logic
    class HttpLogin extends HttpRequest {

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject;
            if (result == null){Toast.makeText(LoginActivity.this,"WRONG USERNAME OR PASSWORD",Toast.LENGTH_LONG).show();}
            else{
                try {
                    jsonObject = new JSONObject(result);
                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userid",jsonObject.getString("userid"));
                    editor.putString("password",jsonObject.getString("password"));
                    editor.putString("name",jsonObject.getString("name"));
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);

                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }}

        }
    }

}