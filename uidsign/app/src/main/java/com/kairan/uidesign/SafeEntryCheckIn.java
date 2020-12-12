package com.kairan.uidesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kairan.uidesign.Utils.HttpRequest;
import com.kairan.uidesign.Utils.ToSharePreferences;

// maybe rename this activity?
public class SafeEntryCheckIn extends AppCompatActivity {
    ImageView backButton;
    TextView checkIn_location_name;
    public String tag = "SHARED";
    static String GET_CHECK_IN_LOCATION_SCAN = "get check in location";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_entry);
        getSupportActionBar().hide();



        //Log.i(tag,"go to scan activity");
        checkIn_location_name = findViewById(R.id.textView_current_location);
        //SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
        //Log.i(tag,sharedPreferences.toString());
        //String checkinlocationnamecardstring = sharedPreferences.getString("checkInLocation","UNDEFINED");
        String checkInLocation_scan = getIntent().getStringExtra(MenuActivity.checkIn_location_intent);
        String checkInLocation_search = getIntent().getStringExtra(SearchActivity.CheckInLocation);
        if(checkInLocation_search==null) {
            //Log.i(tag, checkinlocationnamecardstring);
            checkIn_location_name.setText(checkInLocation_scan);
        }else{
            checkIn_location_name.setText(checkInLocation_search);
        }
        //Log.i(tag,"set text");

        //check in and out buttons
        Button mCheckIn = findViewById(R.id.button_checkIn_safeEntry);
        //Button mCheckOut = findViewById(R.id.button_checkOut_safeEntry);


        //button functions
        mCheckIn.setOnClickListener(v -> {

            //Intent intent = getIntent();
            //String result = intent.getExtras().getString(MenuActivity.checkIn_location_intent);
            // System.out.println("++++++++++++++++++++++++");
            // System.out.println(result);

            /*SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("checkinlocationnamecard",result);
            editor.commit();*/

            // checkinlocationnamecard = findViewById(R.id.textView_current_location);
            // String checkinlocationnamecardstring = sharedPreferences.getString("checkinlocationnamecard","UNDEFINED");;


            HttpCheckIn executeCheckIn = new HttpCheckIn();
            executeCheckIn.execute("checkin",
                    ToSharePreferences.GetSharedPreferences(SafeEntryCheckIn.this,"userid"),
                    ToSharePreferences.GetSharedPreferences(SafeEntryCheckIn.this,"password"),
                    "1",checkIn_location_name.getText().toString());

            Intent successScreen = new Intent(SafeEntryCheckIn.this, CheckInSuccess.class);
            successScreen.putExtra(GET_CHECK_IN_LOCATION_SCAN,checkIn_location_name.getText().toString());
            startActivity(successScreen);
            overridePendingTransition(R.anim.zoom_out,R.anim.zoom_out);
            Log.i(tag,checkIn_location_name.getText().toString());

        });
        /**
         * FOR NICHOLAS TO IMPLEMENT
         */
        /*mCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: make checkout button check out in database
            }
        });

        if(MenuActivity.getScanActivityState() == 1){
            checkinlocationnamecard.setText(checkinlocationnamecardstring);
            mCheckIn.setVisibility(View.GONE);
            mCheckOut.setVisibility(View.VISIBLE);
        }else{
            checkinlocationnamecard.setText(checkinlocationnamecardstring);
            mCheckIn.setVisibility(View.VISIBLE);
            mCheckOut.setVisibility(View.GONE );
        }*/


        backButton = findViewById(R.id.back_from_safeentry);
        backButton.setOnClickListener(v -> {
            Intent mgoBack = new Intent(SafeEntryCheckIn.this, MenuActivity.class);
            startActivity(mgoBack);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
        });




    }
    // HTTPGetRequest Class to handle login network logic
    class HttpCheckIn extends HttpRequest {

        @Override
        protected void onPostExecute(String result) {
            if (result == null){
                Toast.makeText(SafeEntryCheckIn.this,"NO CURRENT CHECKINS",Toast.LENGTH_LONG).show();
            }
            else{
                checkIn_location_name.setText(result);
                //.makeText(SafeEntryCheckIn.this,"Success Checking In.",Toast.LENGTH_LONG).show();
            }
        }
    }

    /*class executeCheckIn extends AsyncTask<String, String, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        private String checkIn_location;

        executeCheckIn(String checkIn_location){
            this.checkIn_location = checkIn_location;
        }


        protected String doInBackground(String... urls){
            //String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
                String useridtosend = sharedPreferences.getString("userid","UNDEFINED");;
                String passwordtosend = sharedPreferences.getString("password","UNDEFINED");;
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("checkinlocationnamecard",urls[0]);
//
//                editor.commit();

                publishProgress(String.valueOf(urls[0]));


                //URL myUrl = new URL("https://somapass.xyz/checkin/"+useridtosend+"/"+passwordtosend+"/1/"+urls[0]);
                URL myUrl = new URL("https://somapass.xyz/checkin/"+useridtosend+"/"+passwordtosend+"/1/"+checkIn_location);
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
                Toast.makeText(SafeEntryCheckIn.this,"NO CURRENT CHECKINS",Toast.LENGTH_LONG).show();
            }
            else{
                checkIn_location_name.setText(result);
                //.makeText(SafeEntryCheckIn.this,"Success Checking In.",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //checkIn_location_name.setText(values[0]);
        }
    }








    // HTTPGetRequest Class to check latest checked in location
    class HttpGetRequestCheckout extends AsyncTask<String, Void, String> {
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

            // TODO: 2/12/2020 make locationname be fetched from QR code instead of entire link
            String locationname = "DSL";
            try {
                //Create a URL object holding our url
                //TODO TO implement the URL Builder taught to us instead of string concat for URL
                URL myUrl = new URL("https://somapass.xyz/checkin/"+useridtosend+"/"+passwordtosend+"/1/"+locationname);
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
                Toast.makeText(SafeEntryCheckIn.this,"NO CURRENT CHECKINS.",Toast.LENGTH_LONG).show();
            }
            else{
                checkIn_location_name.setText(result);
                //Toast.makeText(SafeEntryCheckIn.this,"Success Checking out.",Toast.LENGTH_LONG).show();
                //Intent intent2 = new Intent(SafeEntryCheckIn.this,MainActivity.class);
                //startActivity(intent2);
            }

        }
    }
*/


}