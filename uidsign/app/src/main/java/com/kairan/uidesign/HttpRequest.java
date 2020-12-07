package com.kairan.uidesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

abstract class HttpRequest extends AsyncTask<String,Void,String> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    final String scheme = "https";
    final String authority = "somapass.xyz";
    private String useridtosend;
    private String passwordtosend;
    private String back;
    private Uri uri;

    HttpRequest(String back, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
        useridtosend = sharedPreferences.getString("userid","UNDEFINED");;
        passwordtosend = sharedPreferences.getString("password","UNDEFINED");;
        this.back = back+"/"+useridtosend+"/"+passwordtosend;
        // maybe try a self build url builder
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(scheme)
                .authority(authority)
                .appendPath(this.back);
        uri = builder.build();
    }

    protected String doInBackground(String... params){
        //String stringUrl = params[0];
        String result;
        String inputLine;
        /*SharedPreferences sharedPreferences = getSharedPreferences_("com.example.android.mainsharedprefs", Context.MODE_PRIVATE);
        String useridtosend = sharedPreferences.getString("userid","UNDEFINED");;
        String passwordtosend = sharedPreferences.getString("password","UNDEFINED");;*/
        try {
            //Create a URL object holding our url
            //TODO TO implement the URL Builder taught to us instead of string concat for URL
            //URL myUrl = new URL("https://somapass.xyz/latestcheckout/"+useridtosend+"/"+passwordtosend);
            URL myUrl = new URL("https://somapass.xyz/"+back);
            //URL myUrl= new URL(uri.toString());
            System.out.println("==================================================================");
            System.out.println("===================================================================");
            System.out.println(myUrl);
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
    protected abstract void onPostExecute(String result);

    //protected abstract void makeToast(String text);

    //protected abstract void createIntent();

}
