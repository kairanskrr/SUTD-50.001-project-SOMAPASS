package com.kairan.uidesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Generate sample data

        locationList = new String[]{"Lion", "Tiger", "Dog",
                "Cat", "Tortoise", "Rat", "Elephant", "Fox",
                "Cow","Donkey","Monkey"};


        // Locate the ListView
        list = (ListView) findViewById(R.id.listview);

        for (int i = 0; i < locationList.length; i++) {

            // Binds all strings into an array
            locationArray.add(locationList[i]);

        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, locationArray);


        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in
        editSearch = (SearchView) findViewById(R.id.search);
        editSearch.setOnQueryTextListener(this);
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
