package com.example.dam.uebung3.Util;

import android.telephony.TelephonyManager;

import com.example.dam.uebung3.Model.RadioType;
import com.example.dam.uebung3.Model.Record;

/**
 * Created by Mat on 20.01.2016.
 */
public class Utils {

    public static double calculateGPSDistance(double lat1, double lng1, double lat2, double lng2) {
        double pk = (float) (180.f/Math.PI);

        double a1 = lat1 / pk;
        double a2 = lng1 / pk;
        double b1 = lat2 / pk;
        double b2 = lng2 / pk;

        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3 = Math.sin(a1)*Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000*tt;
    }

    public static RadioType getRadioType(int cellType) {
        switch (cellType) {
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return RadioType.WCDMA;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return RadioType.LTE;
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return RadioType.GMS;
        }

        return null;
    }

    public static String getFilename(Record record) {
        return record.getDate() + "_" + record.getId();
    }
}
