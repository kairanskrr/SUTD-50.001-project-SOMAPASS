package com.kairan.uidesign.Utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

abstract public class HttpRequest extends AsyncTask<String,String,String> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private static final String scheme = "https";
    private static final String authority = "somapass.xyz";
    private Uri uri;




    protected String doInBackground(String... strings){
        String result;
        String inputLine;
        try {
            URL myUrl = getURL(strings);
            Log.i(StringsUsed.TAG,"my URL: " + myUrl.toString());
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

    public URL getURL(String[] strings) throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(scheme)
                .authority(authority);
        for(String i:strings){
            builder.appendPath(i);
        }
        uri = builder.build();
        return new URL(uri.toString());
    }

    protected abstract void onPostExecute(String result);

}
