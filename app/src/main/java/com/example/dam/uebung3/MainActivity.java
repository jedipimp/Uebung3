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
import com.example.dam.uebung3.Model.Record;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends FragmentActivity{


    private ResponseReceiver receiver;
    // Acquire a reference to the system Location Manager
    LocationManager locationManager;
    Location ourLocation;
    private String provider;
    private MainRecordFragment mainRecordFragment;

    double mlsLat;
    double mlsLng;

    private static final String fileName = "records.txt";
    private static final String STORE_CRYPT_KEY = "crypt_key";
    public static final String STORE_MLS_KEY = "mls_key";
    public static final String SHARED_PREFS_FILE = "data";


    // ARRAYLIST TO HOLD THE RECORDFRAGMENTS
    private ArrayList<RecordFragment> recordFragmentList;


    @Override
    public void onStart()
    {
        super.onStart();
        loadRecordFragments();


        Button scan = (Button) findViewById(R.id.scanButtonId);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mlsIntent = new Intent(v.getContext(), MLSIntentService.class);
                startService(mlsIntent);
                int permissionCheck = ContextCompat.checkSelfPermission(v.getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION);

                ourLocation = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                double gpsLat = ourLocation.getLatitude();
                double gpsLng = ourLocation.getLongitude();


                System.out.println("the values before create dare : " + mlsLat + " <-- lat and lng " + mlsLng);

                // create main activity
                MainRecordFragment mainRecordFragment = (MainRecordFragment) getSupportFragmentManager().
                        findFragmentById(R.id.mainRecordFragment);

                mainRecordFragment.setRecord(new Record(mlsLat, mlsLng,
                        ourLocation.getLatitude(), ourLocation.getLongitude(), ourLocation.getAccuracy()));
                mainRecordFragment.refresh();


                // ADD THE FRAGMENT TO THE FRAGMENT CONTAINER
                //getSupportFragmentManager().beginTransaction()
                  //      .add(R.id.linearLayoutRecordsId, mainRecordFragment).commit();

            }
        });

        Button save = (Button) findViewById(R.id.saveButtonId);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //RecordFragment record = RecordFragment.newInstance(mlsLat, mlsLng, gpsLat, gpsLng);
                //recordFragmentList.add(record);
            }
        });

    }


    public void deleteRecordFragment(RecordFragment recordFragment)
    {
        LinearLayout l = (LinearLayout)findViewById(R.id.linearLayoutRecordsId);
        getSupportFragmentManager().beginTransaction()
                .remove(recordFragment).commit();
        recordFragmentList.remove(recordFragment);
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

        recordFragmentList = new ArrayList<RecordFragment>();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        File file = new File(getApplicationContext().getFilesDir(), fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            }catch (IOException e){ System.out.println(e.getMessage());}
        }

        FileOutputStream outputStream;
        try {
            String fileName = new Date().toString();

            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(outputStream);
            for (RecordFragment recordFragment: recordFragmentList)
            {
                Record record = recordFragment.getRecord();

                dos.writeDouble(record.getMlsLat());

                dos.writeDouble(record.getMlsLng());

                dos.writeDouble(record.getGpsLat());

                dos.writeDouble(record.getGpsLng());

                dos.writeFloat(record.getGpsAcc());

                dos.writeUTF(record.getDate().toString());
            }

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRecordFragments() {
        DataInputStream dis;
        File f = new File(getApplicationContext().getFilesDir(),".");
        File[] files = f.listFiles();

        if (files != null) {
            for (File file : files) {

                boolean s = true;
                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    dis = new DataInputStream(inputStream);


                    double mlsLat = dis.readDouble();
                    double mlsLng = dis.readDouble();
                    double gpsLat = dis.readDouble();
                    double gpsLng = dis.readDouble();
                    float gpsAcc = dis.readFloat();

                    Date date = new Date();//Date.pdis.readUTF();

                    RecordFragment rf = RecordFragment.newInstance(mlsLat, mlsLng, gpsLat, gpsLng, gpsAcc);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.linearLayoutRecordsId, rf).commit();


                    // add to the arraylist also


                    dis.close();

                } catch (EOFException eof) {
                    s = false;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
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
