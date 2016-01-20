package com.example.dam.uebung3;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private Button save;
    private SharedPreferences prefs;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String STORE_CRYPT_KEY = "crypt_key";
    public static final String STORE_MLS_KEY = "mls_key";
    public static final String SHARED_PREFS_FILE = "data";
    public static final String STORE_ID_COUNTER = "id_counter";
    public static final String WIFI_MAC_ADDRESSES = "wifi_mac_addresses";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init id counter
        prefs = getSharedPreferences(SHARED_PREFS_FILE,MODE_PRIVATE);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);

        // set main record fragment
        mainRecordFragment = (MainRecordFragment) getSupportFragmentManager().
                findFragmentById(R.id.mainRecordFragment);

        loadRecordFragments();

        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    unregisterReceiver(wifiScanReceiver);

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


        save = (Button) findViewById(R.id.saveButtonId);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record r = mainRecordFragment.getRecord();
                r.setId(getNextRecordId());

                RecordFragment recordFragment = RecordFragment.newInstance(r);

                // save to file
                if (saveRecordFragment(recordFragment)) {
                    // create new record fragment if record doesnt exists already
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.linearLayoutRecordsId, recordFragment).commit();
                }
                save.setEnabled(false);
                mainRecordFragment.clear();
            }
        });

        Button scan = (Button) findViewById(R.id.scanButtonId);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear fields of main fragment
                mainRecordFragment.clear();
                save.setEnabled(false);

                int permissionCheck = ContextCompat.checkSelfPermission(v.getContext(),
                        Manifest.permission.CHANGE_WIFI_STATE);

                // start WIFI search
                registerReceiver(wifiScanReceiver,
                        new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
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
            }
        });
    }

    public void deleteRecordFragment(RecordFragment recordFragment)
    {
        LinearLayout l = (LinearLayout)findViewById(R.id.linearLayoutRecordsId);
        getSupportFragmentManager().beginTransaction()
                .remove(recordFragment).commit();

        // delete file
        File file = new File(getApplicationContext().getFilesDir(),
                Utils.getFilename(recordFragment.getRecord()));
        file.delete();
        System.out.println("Deleted file: " + file);
    }

    public boolean saveRecordFragment(RecordFragment recordFragment) {
        Record record = recordFragment.getRecord();
        String filename = Utils.getFilename(record);

        File file = new File(getApplicationContext().getFilesDir(), filename);

        if (!file.exists()) {
            try {
                file.createNewFile();
            }catch (IOException e){ System.out.println(e.getMessage());}
        } else {
            return false;
        }

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(outputStream);

            dos.writeInt(record.getId());

            dos.writeDouble(record.getMlsLat());

            dos.writeDouble(record.getMlsLng());

            dos.writeDouble(record.getGpsLat());

            dos.writeDouble(record.getGpsLng());

            dos.writeFloat(record.getGpsAcc());

            dos.writeDouble(record.getDistance());

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = dateFormatter.format(record.getDate());

            dos.writeUTF(dateStr);

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public void loadRecordFragments() {
        DataInputStream dis;
        File f = new File(getApplicationContext().getFilesDir(),".");
        File[] files = f.listFiles();
        System.out.println("FILES size: " + files.length);
        if (files != null) {
            for (File file : files) {
                System.out.println("FILE: " + file);
                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    dis = new DataInputStream(inputStream);

                    int id = dis.readInt();
                    double mlsLat = dis.readDouble();
                    double mlsLng = dis.readDouble();
                    double gpsLat = dis.readDouble();
                    double gpsLng = dis.readDouble();
                    float gpsAcc = dis.readFloat();
                    double distance = dis.readDouble();

                    String dateStr = dis.readUTF();
                    Date date = dateFormatter.parse(dateStr);

                    RecordFragment rf = RecordFragment.newInstance(new Record(id,mlsLat,mlsLng,gpsLat,gpsLng,gpsAcc,distance,date));
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.linearLayoutRecordsId, rf).commit();

                    dis.close();

                } catch (EOFException eof) {
                    eof.printStackTrace();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (ParseException pe) {
                    pe.printStackTrace();
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
            // enable save button
            save.setEnabled(true);
        }
    }

    private int getNextRecordId() {
        // get previous saved record id from shared preferences
        int idCounter = prefs.getInt(STORE_ID_COUNTER,0);
        idCounter ++;
        // save new id to shared prefs
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(MainActivity.STORE_ID_COUNTER, idCounter);
        editor.commit();

        return idCounter;
    }
}
