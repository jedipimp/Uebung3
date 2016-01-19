package com.example.dam.uebung3.Model;

import android.util.FloatMath;

import java.util.Date;

/**
 * Created by dam on 11/01/16.
 */
public class Record {

    private static int idCounter = 0;

    private final int id;
    private double mlsLat;
    private double mlsLng;

    private double gpsLat;
    private double gpsLng;
    private float gpsAcc;

    private double distance;
    private Date date;

    public Record()
    {
        id = idCounter;
        ++idCounter;

    }

    public Record(double mlslat, double mlsLng, double gpslat, double gpsLng, float gpsAcc)
    {
        date = new Date();
        id = idCounter;
        ++idCounter;

        this.mlsLat = mlslat;
        this.mlsLng = mlsLng;
        this.gpsLat = gpslat;
        this.gpsLng = gpsLng;
        this.gpsAcc = gpsAcc;

        distance = calculateDistance();
    }

    public void setMlsLat(double mlsLat) {this.mlsLat = mlsLat; }

    public double getMlsLat() {return mlsLat;}

    public void setMlsLng(double mlsLng) {this.mlsLng = mlsLng; }

    public double getMlsLng() {return mlsLng;}

    public void setGpsLat(double gpsLat) {this.gpsLat = gpsLat; }

    public double getGpsLat() {return gpsLat;}

    public void setGpsLng(double gpsLng) {this.gpsLng = gpsLng; }

    public double getGpsLng() {return gpsLng;}

    public void setGpsAcc(float gpsAcc) {this.gpsAcc = gpsAcc; }

    public float getGpsAcc() {return gpsAcc;}

    public Date getDate() {return date;}

    public void setDate(Date date) {this.date = date;}

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
