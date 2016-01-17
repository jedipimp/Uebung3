package com.example.dam.uebung3;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import com.example.dam.uebung3.MainActivity.ResponseReceiver;
import org.json.JSONObject;

/**
 * Created by dam on 16/01/16.
 */
public class MLSIntentService extends IntentService {

    public static  final String PARAM_OUT_MSG = "omsg";

    URL url;
    HttpURLConnection connection;

    public MLSIntentService()
    {

        super("MLSIntent Service");


    }
    @Override
    protected void onHandleIntent(Intent intent) {
        try{

            url = new URL("https://location.services.mozilla.com/v1/geolocate?key=test");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();

            //build the string to store the response text from the server
            String response = "";
            //start listening to the stream
            Scanner inStream = new Scanner(connection.getInputStream());



//process the stream and store it in StringBuilder
            while(inStream.hasNextLine())
                response+=(inStream.nextLine());


            /////////////////////////

            String in = response;
            JSONObject reader = new JSONObject(in);


            JSONObject loc  = reader.getJSONObject("location");
            String lat = loc.getString("lat");


            String lng = loc.getString("lng");

            // I WILL RETURN A STRING CONTAINING LAT AND LONG SEPERATED BY A SPACE
            String sendBack = lat+ " "+ lng;


            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(PARAM_OUT_MSG, sendBack);
            sendBroadcast(broadcastIntent);


        }catch (MalformedURLException e){Log.d("MyAppddd", "URL exception");} catch(Exception e ){Log.d("MyAppddd", e.getMessage());}
    }
}
