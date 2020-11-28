package com.kairan.uidesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<String> locationList = null;
    private ArrayList<String> locationArrayList;
    private ArrayList<Boolean> booleanArray;

    public ListViewAdapter(Context context, List<String> locationList) {
        mContext = context;
        this.locationList = locationList;
        inflater = LayoutInflater.from(mContext);
        this.locationArrayList = new ArrayList<String>();
        this.locationArrayList.addAll(locationList);
        // if booleanarray exists in sharedpreferances, read that, else make a new booleanarray
        /*if (booleanarray not in shared preferances) {
            for (int i=0;i<locationArrayList.size();i++){
                booleanArray.add(false);
            }
        } else {
            readthatshit();

        }*/
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
            holder.location = (TextView) view.findViewById(R.id.location);
            holder.checkbox = (CheckBox) view.findViewById(R.id.listCheckbox);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.location.setText(locationList.get(position));

        return view;
    }

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

    public void getBooleanArray(ArrayList<Boolean> booleanArray) {
        this.booleanArray = booleanArray;
    }

    public ArrayList<Boolean> setBooleanArray(){
        return this.booleanArray;
    }
}

