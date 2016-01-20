package com.example.dam.uebung3;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.dam.uebung3.Model.Record;
import com.example.dam.uebung3.Util.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MainActivity extends FragmentActivity{


    private ResponseReceiver receiver;
    // Acquire a reference to the system Location Manager
    LocationManager locationManager;
    Location ourLocation;
    private String provider;
    private MainRecordFragment mainRecordFragment;
    private WifiManager wifiManager;
    private BroadcastReceiver wifiScanReceiver;

    private static final String fileName = "records.txt";
    private static final String STORE_CRYPT_KEY = "crypt_key";
    public static final String STORE_MLS_KEY = "mls_key";
    public static final String SHARED_PREFS_FILE = "data";
    public static final String WIFI_MAC_ADDRESSES = "wifi_mac_addresses";


    // ARRAYLIST TO HOLD THE RECORDFRAGMENTS
    private ArrayList<RecordFragment> recordFragmentList;


    @Override
    public void onStart()
    {
        super.onStart();
        loadRecordFragments();

        // set main record fragment
        mainRecordFragment = (MainRecordFragment) getSupportFragmentManager().
                findFragmentById(R.id.mainRecordFragment);

        // setup WIFI
        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    List<ScanResult> scanResults = wifiManager.getScanResults();

                    String[] macAddresses = new String[scanResults.size()];
                    int i = 0;
                    for (ScanResult sr : scanResults) {
                        macAddresses[i] = sr.BSSID;
                        i++;
                    }
                    System.out.println("Scanned MAC-Addresses: " + Arrays.toString(macAddresses));
                    // start intent MLS WebService Request
                    Intent mlsIntent = new Intent(c, MLSIntentService.class);
                    mlsIntent.putExtra(WIFI_MAC_ADDRESSES, macAddresses);
                    startService(mlsIntent);
                }
            }
        };

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        registerReceiver(wifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));



        Button scan = (Button) findViewById(R.id.scanButtonId);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear fields of main fragment
                mainRecordFragment.clear();

                int permissionCheck = ContextCompat.checkSelfPermission(v.getContext(),
                        Manifest.permission.CHANGE_WIFI_STATE);

                // start WIFI search
                wifiManager.startScan();



                // get GPS position
                permissionCheck = ContextCompat.checkSelfPermission(v.getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION);

                ourLocation = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                double gpsLat = ourLocation.getLatitude();
                double gpsLng = ourLocation.getLongitude();

                mainRecordFragment.getRecord().setDate(new Date());

                // set GPS coordinates
                mainRecordFragment.getRecord().setGpsLat(ourLocation.getLatitude());
                mainRecordFragment.getRecord().setGpsLng(ourLocation.getLongitude());
                mainRecordFragment.getRecord().setGpsAcc(ourLocation.getAccuracy());

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
            double mlsLat = Double.parseDouble(seperate[0]);
            double mlsLng = Double.parseDouble(seperate[1]);

            // set MLS coordinates
            mainRecordFragment.getRecord().setMlsLat(mlsLat);
            mainRecordFragment.getRecord().setMlsLng(mlsLng);


            // set distance between MLS and GPS
            mainRecordFragment.getRecord().setDistance(
                    Utils.calculateGPSDistance(mlsLat, mlsLng,
                            mainRecordFragment.getRecord().getGpsLat(),
                            mainRecordFragment.getRecord().getGpsLng())
            );

            mainRecordFragment.refresh();
        }
    }
}
