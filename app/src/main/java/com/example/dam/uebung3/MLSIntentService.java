package com.example.dam.uebung3;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import com.example.dam.uebung3.MainActivity.ResponseReceiver;
import com.example.dam.uebung3.Model.CellTower;
import com.example.dam.uebung3.Model.LocationResponse;
import com.example.dam.uebung3.Model.MLSGeolocateBody;
import com.example.dam.uebung3.Model.RadioType;
import com.example.dam.uebung3.Model.WifiAccessPoint;
import com.example.dam.uebung3.Util.Utils;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;


/**
 * Created by dam on 16/01/16.
 */
public class MLSIntentService extends IntentService {

    public static final String PARAM_OUT_MSG = "omsg";

    URL url;
    HttpURLConnection connection;



    public MLSIntentService()
    {

        super("MLSIntent Service");


    }
    @Override
    protected void onHandleIntent(Intent intent) {

        // TODO: user input for key and save it in shared prefs
        String mlsKey = "test";




        LocationResponse locationResponse = null;

        MLSGeolocateBody body = new MLSGeolocateBody();
        // get WIFI infos
        List<WifiAccessPoint> wapList = new ArrayList<>();

        String[] wifiMacAddresses = intent.getStringArrayExtra(MainActivity.WIFI_MAC_ADDRESSES);
        for (String macAddress : wifiMacAddresses) {
            WifiAccessPoint wap = new WifiAccessPoint();
            wap.setMacAddress(macAddress);
            wapList.add(wap);
        }

        // get cell tower infos
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();

        List<CellTower> cellTowerList = new ArrayList<>();

        if (cellInfos != null) {
            for (CellInfo ci : cellInfos) {
                CellTower cellTower = new CellTower();
                // / UMTS (= WCDMA)
                if (ci instanceof CellInfoWcdma) {
                    CellInfoWcdma ciWcdma = (CellInfoWcdma) ci;
                    cellTower.setRadioType(RadioType.WCDMA);
                    cellTower.setCellId(ciWcdma.getCellIdentity().getCid());
                    cellTower.setLocationAreaCode(ciWcdma.getCellIdentity().getLac());
                    cellTower.setMobileCountryCode(ciWcdma.getCellIdentity().getMcc());
                    cellTower.setMobileNetworkCode(ciWcdma.getCellIdentity().getMnc());
                    cellTower.setPsc(ciWcdma.getCellIdentity().getPsc());
                    cellTower.setSignalStrength(ciWcdma.getCellSignalStrength().getDbm());
                    //LTE
                } else if (ci instanceof CellInfoLte) {
                    CellInfoLte ciLte = (CellInfoLte) ci;
                    cellTower.setRadioType(RadioType.LTE);
                    cellTower.setCellId(ciLte.getCellIdentity().getCi());
                    cellTower.setMobileNetworkCode(ciLte.getCellIdentity().getMnc());
                    cellTower.setMobileCountryCode(ciLte.getCellIdentity().getMcc());
                    cellTower.setSignalStrength(ciLte.getCellSignalStrength().getDbm());
                    // GSM
                } else if (ci instanceof CellInfoGsm) {
                    CellInfoGsm ciGsm = (CellInfoGsm) ci;
                    cellTower.setRadioType(RadioType.GMS);
                    cellTower.setCellId(ciGsm.getCellIdentity().getCid());
                    cellTower.setMobileCountryCode(ciGsm.getCellIdentity().getMcc());
                    cellTower.setMobileNetworkCode(ciGsm.getCellIdentity().getMnc());
                    cellTower.setLocationAreaCode(ciGsm.getCellIdentity().getLac());
                    cellTower.setSignalStrength(ciGsm.getCellSignalStrength().getDbm());
                }

                cellTowerList.add(cellTower);
            }
        } else {
            GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            CellTower cellTower = new CellTower();
            cellTower.setCellId(cellLocation.getCid());
            cellTower.setLocationAreaCode(cellLocation.getLac());
            cellTower.setPsc(cellLocation.getPsc());

            String networkOperator = telephonyManager.getNetworkOperator();
            cellTower.setMobileCountryCode(new Integer(networkOperator.substring(0, 3)));
            cellTower.setMobileNetworkCode(new Integer(networkOperator.substring(3)));
            cellTower.setRadioType(Utils.getRadioType(telephonyManager.getNetworkType()));

            body.setCarrier(telephonyManager.getNetworkOperatorName());
            body.setHomeMobileCountryCode(new Integer(networkOperator.substring(0, 3)));
            body.setHomeMobileNetworkCode(new Integer(networkOperator.substring(3)));

            cellTowerList.add(cellTower);
        }


        body.setCellTowers(cellTowerList);
        body.setWifiAccessPoints(wapList);


        Gson gson = new Gson();
        String jsonRequest = gson.toJson(body);

        System.out.println("JSON Request: " + jsonRequest);

        try {

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost request = new HttpPost("https://location.services.mozilla.com/v1/geolocate?key=test");

            StringEntity stringEntity = new StringEntity(jsonRequest);
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(stringEntity);
            HttpResponse response = httpclient.execute(request);

            String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");

            System.out.println("JSON Response: " + jsonResponse);
            locationResponse = gson.fromJson(jsonResponse, LocationResponse.class);
            System.out.println("LocationResponse: " + locationResponse);
            //start listening to the stream
           /* Scanner inStream = new Scanner(connection.getInputStream());
            String response = "";
            //process the stream and store it in StringBuilder
            while(inStream.hasNextLine())
                response+=(inStream.nextLine());

            String in = response;
            JSONObject reader = new JSONObject(in);


            JSONObject loc  = reader.getJSONObject("location");
            String lat = loc.getString("lat");


            String lng = loc.getString("lng");
            Log.d("lat: ", lat);
            Log.d("lng: ", lng);*/
        } catch (Exception e) {
            e.printStackTrace();
        }


        double lat = 0;
        double lng = 0;
        //try {
            //response = call.execute();
            //System.out.println("RESPONSE MSG: " + response.message());
            //locationResponse = response.body();

            if (locationResponse != null) {
                lat = locationResponse.getLocation().getLat();
                lng = locationResponse.getLocation().getLng();
            } else {
                System.out.println("locationResponse == null");
            }
        /*} catch (IOException e) {
            e.printStackTrace();
        }*/


        // I WILL RETURN A STRING CONTAINING LAT AND LONG SEPERATED BY A SPACE
        String sendBack = lat+ " "+ lng;


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(PARAM_OUT_MSG, sendBack);
        sendBroadcast(broadcastIntent);

    }

}
