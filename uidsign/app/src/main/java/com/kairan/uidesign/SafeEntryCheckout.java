package com.kairan.uidesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kairan.uidesign.Utils.HttpRequest;
import com.kairan.uidesign.Utils.ToSharePreferences;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SafeEntryCheckout extends AppCompatActivity {
    TextView locationanamecard;
    Button checkOutButton;
    public String tag = "SHARED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_entry_checkout);
        getSupportActionBar().hide();
        locationanamecard = findViewById(R.id.textView_current_location_out);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
        String checkoutlocationnamecard = sharedPreferences.getString("checkoutlocationnamecard","UNDEFINED");;
        locationanamecard.setText(checkoutlocationnamecard);

        checkOutButton = findViewById(R.id.button_checkOut_safeEntry);
        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NETWORK REQ AND EXECUTE
                //SafeEntryCheckout.HttpGetRequestCheckout httpreqcheckout = new SafeEntryCheckout.HttpGetRequestCheckout();
                //httpreqcheckout.execute();
                HttpReqCheckout httpReqCheckout = new HttpReqCheckout();
                httpReqCheckout.execute("latestcheckout",
                        ToSharePreferences.GetSharedPreferences(SafeEntryCheckout.this,"userid"),
                        ToSharePreferences.GetSharedPreferences(SafeEntryCheckout.this,"password"));
            }
        });



    }

    class HttpReqCheckout extends HttpRequest{

        @Override
        protected void onPostExecute(String result) {
            if (result == null){
                Toast.makeText(SafeEntryCheckout.this,"NO CHECKINS.",Toast.LENGTH_LONG).show();
            }
            else{
                //Toast.makeText(SafeEntryCheckout.this,"Success Checking out.",Toast.LENGTH_LONG).show();
                Intent intent_after_checkout = new Intent(SafeEntryCheckout.this,MenuActivity.class);
                startActivity(intent_after_checkout);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        }
    }
    // HTTPGetRequest Class to check latest checked in location
    /*class HttpGetRequestCheckout extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        protected String doInBackground(String... params){
            //String stringUrl = params[0];
            String result;
            String inputLine;
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
            String useridtosend = sharedPreferences.getString("userid","UNDEFINED");;
            String passwordtosend = sharedPreferences.getString("password","UNDEFINED");;
            try {
                //Create a URL object holding our url
                //TODO TO implement the URL Builder taught to us instead of string concat for URL
                URL myUrl = new URL("https://somapass.xyz/latestcheckout/"+useridtosend+"/"+passwordtosend);
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
            if (result == null){
                Toast.makeText(SafeEntryCheckout.this,"NO CHECKINS.",Toast.LENGTH_LONG).show();
            }
            else{
                //Toast.makeText(SafeEntryCheckout.this,"Success Checking out.",Toast.LENGTH_LONG).show();
                Intent intent_after_checkout = new Intent(SafeEntryCheckout.this,MenuActivity.class);
                startActivity(intent_after_checkout);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }

        }
    }*/




    //end of latestCheckIn

    //end of checkout button
}