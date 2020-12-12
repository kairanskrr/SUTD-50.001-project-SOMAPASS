package com.kairan.uidesign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kairan.uidesign.Utils.HttpRequest;
import com.kairan.uidesign.Utils.StringsUsed;
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



        Log.i(StringsUsed.TAG,"go to safe entry activity");
        checkIn_location_name = findViewById(R.id.textView_current_location);
        String checkInLocation_scan = getIntent().getStringExtra(MenuActivity.checkIn_location_intent);
        String checkInLocation_search = getIntent().getStringExtra(SearchActivity.CheckInLocation);
        if(checkInLocation_search==null) {
            checkIn_location_name.setText(checkInLocation_scan);
        }else{
            checkIn_location_name.setText(checkInLocation_search);
        }

        //check in buttons
        Button mCheckIn = findViewById(R.id.button_checkIn_safeEntry);

        mCheckIn.setOnClickListener(v -> {
            HttpCheckIn executeCheckIn = new HttpCheckIn();
            executeCheckIn.execute(StringsUsed.CheckIn_http,
                    ToSharePreferences.GetSharedPreferences(SafeEntryCheckIn.this,StringsUsed.user_id_sp),
                    ToSharePreferences.GetSharedPreferences(SafeEntryCheckIn.this,StringsUsed.user_password_sp),
                    "1",checkIn_location_name.getText().toString());

            Intent successScreen = new Intent(SafeEntryCheckIn.this, CheckInSuccess.class);
            successScreen.putExtra(GET_CHECK_IN_LOCATION_SCAN,checkIn_location_name.getText().toString());
            startActivity(successScreen);
            overridePendingTransition(R.anim.zoom_out,R.anim.zoom_out);
            Log.i(tag,checkIn_location_name.getText().toString());

        });

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
            }
        }
    }


}