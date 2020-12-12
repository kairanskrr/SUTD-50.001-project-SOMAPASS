package com.kairan.uidesign;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class ListViewAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<String> locationList = null;
    private ArrayList<String> locationArrayList;
    private ArrayList<Boolean> booleanArray;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public ListViewAdapter(Context context, List<String> locationList) {
        mContext = context;
        this.locationList = locationList;
        inflater = LayoutInflater.from(mContext);
        this.locationArrayList = new ArrayList<String>();
        this.locationArrayList.addAll(locationList);
        this.sharedPreferences = mContext.getSharedPreferences("com.example.android.searchsharedprefs", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public class ViewHolder {
        TextView location;
        CheckBox checkbox;
    }

    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public String getItem(int position) {
        return locationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);

            // Locate the TextViews in listview_item.xml
            holder.location = (TextView) view.findViewById(R.id.textView_location_search);
            holder.checkbox = (CheckBox) view.findViewById(R.id.listCheckbox);
            holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {

                    //add to sharedpref
                    if (sharedPreferences.contains("starred locations")) {
                        editor.putString("starred locations",  holder.location.getText().toString()+ ","+sharedPreferences.getString("starred locations", "") );

                    } else {
                        editor.putString("starred locations", holder.location.getText().toString()+ ",");
                    }

                } else {
                    //remove from sharedpref
                    editor.putString("starred locations", sharedPreferences.getString("starred locations", "").replace(holder.location.getText().toString() + ",", ""));
                }
                editor.commit();
            });
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.location.setText(locationList.get(position));
        for (String location: locationList) {
            if (sharedPreferences.getString("starred locations","").contains(location)){
                if (holder.location.getText().toString() == location) {
                    holder.checkbox.setChecked(true);
                }

            }
        }
        holder.location.setOnClickListener(v -> {

            //check into location
            String checkInLocation = holder.location.getText().toString();
            createIntent(checkInLocation);
        });

        return view;
    }

    abstract void createIntent(String location);



    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        locationList.clear();
        if (charText.length() == 0) {
            locationList.addAll(locationArrayList);
        } else {
            for (String wp : locationArrayList) {
                if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    locationList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}

