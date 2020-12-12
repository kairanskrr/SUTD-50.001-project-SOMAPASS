package com.kairan.uidesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kairan.uidesign.Utils.HttpRequest;
import com.kairan.uidesign.Utils.StringsUsed;
import com.kairan.uidesign.Utils.ToSharePreferences;

public class SafeEntryCheckout extends AppCompatActivity {
    TextView locationanamecard;
    Button checkOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_entry_checkout);
        getSupportActionBar().hide();
        locationanamecard = findViewById(R.id.textView_current_location_out);
        SharedPreferences sharedPreferences = getSharedPreferences(StringsUsed.pref_file_sp, Context.MODE_PRIVATE);
        String checkoutlocationnamecard = sharedPreferences.getString(StringsUsed.checkoutLocation_sp,StringsUsed.undefined_sp);;
        locationanamecard.setText(checkoutlocationnamecard);

        checkOutButton = findViewById(R.id.button_checkOut_safeEntry);
        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NETWORK REQ AND EXECUTE
                HttpReqCheckout httpReqCheckout = new HttpReqCheckout();
                httpReqCheckout.execute(StringsUsed.LatestCheckout_http,
                        ToSharePreferences.GetSharedPreferences(SafeEntryCheckout.this,StringsUsed.user_id_sp),
                        ToSharePreferences.GetSharedPreferences(SafeEntryCheckout.this,StringsUsed.user_password_sp));
            }
        });
    }

    // HTTPGetRequest Class to check latest checked in location
    class HttpReqCheckout extends HttpRequest {

        @Override
        protected void onPostExecute(String result) {
            if (result == null){
                Toast.makeText(SafeEntryCheckout.this,"NO CHECKINS.",Toast.LENGTH_LONG).show();
            }
            else{
                Intent intent_after_checkout = new Intent(SafeEntryCheckout.this,MenuActivity.class);
                startActivity(intent_after_checkout);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        }
    }
}