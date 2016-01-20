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

    public double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance) { this.distance = distance; }

}
