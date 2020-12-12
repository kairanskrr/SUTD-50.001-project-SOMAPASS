package com.kairan.uidesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.kairan.uidesign.Utils.HttpRequest;
import com.kairan.uidesign.Utils.StringsUsed;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText editText_username;
    EditText editText_passward;
    CheckBox checkBox_rememberME;
    Button button_login;
    Button button_create_account;

    private final String sharedPrefFile = "com.example.android.mainsharedprefs"; public static final String KEY = "MyKey"; SharedPreferences mPreferences;
//test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //To hide top ActionBar
        getSupportActionBar().hide();

        editText_username = findViewById(R.id.editText_username);
        editText_passward = findViewById(R.id.editText_password);
        checkBox_rememberME = findViewById(R.id.checkBox_rememberMe);
        button_login = findViewById(R.id.button_login);
        button_create_account = findViewById(R.id.button_create_account);

        //Click create account button: to createAccount
        button_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            //TODO REFACTOR Login logic LATER
            @Override
            public void onClick(View v) {
                HttpLogin httpreq = new HttpLogin();
                httpreq.execute(StringsUsed.Login_http,editText_username.getText().toString(),editText_passward.getText().toString());
            }
            //TODO END OF REFACTOR LATER
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
                    //Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }}

        }
    }

    /*class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        protected String doInBackground(String... params){
            //String stringUrl = params[0];
            String result;
            String inputLine;
            String useridtosend = params[0];
            String passwordtosend = params[1];
            try {
                //Create a URL object holding our url
                //TODO TO implement the URL Builder taught to us instead of string concat for URL
                URL myUrl = new URL("https://somapass.xyz/login/"+useridtosend+"/"+passwordtosend);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
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
                    //Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);


                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }}

        }
    }*/

}