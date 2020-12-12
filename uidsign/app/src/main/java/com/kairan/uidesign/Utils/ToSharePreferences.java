package com.kairan.uidesign.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ToSharePreferences {

    private final static String sharedFile = "com.example.android.mainsharedprefs";


    public static String GetSharedPreferences(Context context, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedFile, Context.MODE_PRIVATE);
        return sharedPreferences.getString(name,"UNDEFINED");
    }
}
