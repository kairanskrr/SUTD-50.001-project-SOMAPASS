package com.kairan.uidesign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.kairan.uidesign.Utils.StringsUsed;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ListView list;
    ListViewAdapter adapter;
    SearchView editSearch;
    String[] locationList;
    ArrayList<String> locationArray = new ArrayList<>();
    public static String CheckInLocation = "CHECK_IN_LOCATION";
    final int CHECK_IN_LOCATION_SEARCH = 1110;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        // Generate sample data
        locationList = new String[]{"DSL","DANCE STUDIO 8","COHORT CLASSROOM 8", "CANTEEN","CHEMISTRY LAB","PI LAB","PHYSICS LAB","STUDIO 1","DANCE STUDIO 9","ONE STOP CENTRE","ISH 1"};
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Locate the ListView
        list = findViewById(R.id.listView_search_result);

        for (int i = 0; i < locationList.length; i++) {

            // Binds all strings into an array
            locationArray.add(locationList[i]);

        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, locationArray) {
            @Override
            void createIntent(String location) {
                Intent intent = new Intent(SearchActivity.this, SafeEntryCheckIn.class);
                intent.putExtra(CheckInLocation,location);
                startActivityForResult(intent,CHECK_IN_LOCATION_SEARCH);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        };

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in
        editSearch = (SearchView) findViewById(R.id.search);
        editSearch.setOnQueryTextListener(this);

        list.setOnItemClickListener((parent, view, position, id) -> {
            Log.i("click",parent.getSelectedItem().toString());
            Intent intent = new Intent(SearchActivity.this, SafeEntryCheckIn.class);
            Log.i(StringsUsed.TAG,parent.getItemAtPosition(position).toString());
            intent.putExtra(CheckInLocation,parent.getSelectedItem().toString());
            startActivity(intent);

        });

    }
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }



}
