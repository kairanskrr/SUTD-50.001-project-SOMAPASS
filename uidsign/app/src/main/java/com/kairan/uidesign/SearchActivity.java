package com.kairan.uidesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    SearchView editSearch;
    String[] locationList;
    ArrayList<String> locationArray = new ArrayList<String>();
    public static String CheckInLocation = "CHECK_IN_LOCATION";
    final int CHECK_IN_LOCATION_SEARCH = 1110;
    ArrayAdapter<String> arrayAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        // Generate sample data

        locationList = new String[]{"DSL","DANCE STUDIO 8","COHORT CLASSROOM 8", "CANTEEN","CHEMISTRY LAB","PI LAB","PHYSICS LAB","STUDIO 1","DANCE STUDIO 9","ONE STOP CENTRE","ISH 1"};
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Locate the ListView
        list = (ListView) findViewById(R.id.listView_search_result);

        for (int i = 0; i < locationList.length; i++) {

            // Binds all strings into an array
            locationArray.add(locationList[i]);

        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, locationArray) {
            @Override
            void createIntent(String location) {
                Intent intent = new Intent(SearchActivity.this,ScanActivity.class);
                intent.putExtra(CheckInLocation,location);
                startActivityForResult(intent,CHECK_IN_LOCATION_SEARCH);
            }
        };




        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in
        editSearch = (SearchView) findViewById(R.id.search);
        editSearch.setOnQueryTextListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("click",parent.getSelectedItem().toString());
                Intent intent = new Intent(SearchActivity.this,ScanActivity.class);
                System.out.println("======================================================");
                System.out.println("======================================================");
                System.out.println("======================================================");
                System.out.println(parent.getSelectedItem().toString());
                System.out.println(parent.getItemAtPosition(position).toString());
                intent.putExtra(CheckInLocation,parent.getSelectedItem().toString());
                startActivity(intent);

            }
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
