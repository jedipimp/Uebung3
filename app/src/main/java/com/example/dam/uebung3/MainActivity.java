package com.example.dam.uebung3;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity{


    private ResponseReceiver receiver;
    // Acquire a reference to the system Location Manager
    LocationManager locationManager;
    Location ourLocation;
    private String provider;

    double mlsLat;
    double mlsLng;



    // ARRAYLIST TO HOLD THE RECORDFRAGMENTS
    private ArrayList<RecordFragment> recordFragmentList;


    @Override
    public void onStart()
    {
        super.onStart();
        Button button= (Button) findViewById(R.id.scanAndSaveButtonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mlsIntent = new Intent(v.getContext(), MLSIntentService.class);
                startService(mlsIntent);
                int permissionCheck = ContextCompat.checkSelfPermission(v.getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION);

                ourLocation = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                double gpsLat = ourLocation.getLatitude();
                double gpsLng = ourLocation.getLongitude();


                System.out.println("the values before create dare : "+mlsLat+ " <-- lat and lng "+mlsLng);



                RecordFragment record = RecordFragment.newInstance(mlsLat,mlsLng,gpsLat,gpsLng);

                // ADD THE FRAGMENT TO THE FRAGMENT CONTAINER
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.linearLayoutRecordsId, record).commit();

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "com.example.intent.action.MESSAGE_PROCESSED";
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(MLSIntentService.PARAM_OUT_MSG);
            String[] seperate = text.split(" ");
            mlsLat = Double.parseDouble(seperate[0]);
            mlsLng = Double.parseDouble(seperate[1]);

            System.out.println("MLS-LAT : "+mlsLat);
            System.out.println("MLS-LNG : "+mlsLng);
        }
    }
}
