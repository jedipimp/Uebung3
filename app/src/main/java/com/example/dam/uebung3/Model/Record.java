package com.example.dam.uebung3.Model;

import android.util.FloatMath;

import java.util.Date;

/**
 * Created by dam on 11/01/16.
 */
public class Record {

    private final Date date;
    private double mlsLat;
    private double mlsLng;

    private double gpsLat;
    private double gpsLng;

    private static int idCounter = 0;
    private final int id;


    private double distance;

    public Record()
    {
        date = new Date();
        id = idCounter;
        ++idCounter;

    }

    public Record(double mlslat, double mlsLng, double gpslat, double gpsLng)
    {
        date = new Date();
        id = idCounter;
        ++idCounter;

        this.mlsLat = mlslat;
        this.mlsLng = mlsLng;
        this.gpsLat = gpslat;
        this.gpsLng = gpsLng;

        distance = calculateDistance();
    }

    public double getMlsLat()
    {
        return mlsLat;
    }

    public double getMlsLng()
    {
        return mlsLng;
    }

    public double getGpsLat()
    {
        return gpsLat;
    }

    public double getGpsLng()
    {
        return gpsLng;
    }


    public double calculateDistance()
    {
        double pk = (float) (180.f/Math.PI);

        double a1 = mlsLat / pk;
        double a2 = mlsLng / pk;
        double b1 = gpsLat / pk;
        double b2 = gpsLng / pk;

        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3 = Math.sin(a1)*Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000*tt;
    }

    public double getDistance()
    {
        return distance;
    }



}
