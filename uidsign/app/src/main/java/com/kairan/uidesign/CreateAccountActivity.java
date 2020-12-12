package com.kairan.uidesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kairan.uidesign.Utils.HttpRequest;
import com.kairan.uidesign.Utils.StringsUsed;

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

        // back to login button
        back_to_login = findViewById(R.id.back_to_login);
        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        });


        student_id = findViewById(R.id.editText_name_create_account);
        password = findViewById(R.id.editText_password_create_account);
        name = findViewById(R.id.editText_studentname_create_account);
        //Button to Create Account Here
        create_account_button = findViewById(R.id.create_account_button);
        create_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpReqCreateAccount httpreqcreate = new HttpReqCreateAccount();
                httpreqcreate.execute(StringsUsed.CreateAccount_http,student_id.getText().toString(),password.getText().toString(),name.getText().toString());
            }
        });
    }

    // http request for creating account
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
    /*class HttpGetRequestCreateAccount extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        protected String doInBackground(String... params){
            //String stringUrl = params[0];
            String result;
            String inputLine;
            String useridtosend = params[0];
            String passwordtosend = params[1];
            String nametosend = params[2];
            try {
                //Create a URL object holding our url
                //TODO TO implement the URL Builder taught to us instead of string concat for URL
                URL myUrl = new URL("https://somapass.xyz/createaccount/"+useridtosend+"/"+passwordtosend+"/"+nametosend);
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
            //JSONObject jsonObject;
            if (result == null){
                Toast.makeText(CreateAccountActivity.this,"Error, Please fill in both Student ID and Password or check network",Toast.LENGTH_LONG).show();}
            else{
                try {
                    //jsonObject = new JSONObject(result);
//                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("userid",jsonObject.getString("userid"));
//                    editor.putString("password",jsonObject.getString("password"));
//                    editor.putString("name",jsonObject.getString("name"));
//                    editor.commit();
//                    Toast.makeText(CreateAccountActivity.this, result, Toast.LENGTH_SHORT).show();
                    Intent intent_create_success = new Intent(CreateAccountActivity.this,LoginActivity.class);
                    startActivity(intent_create_success);


                }catch (Exception err){
                    Log.d("Error", err.toString());
                }}

        }
    }*/

}