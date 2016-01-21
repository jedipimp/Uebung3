package com.example.dam.uebung3.Model;

import android.content.SharedPreferences;
import android.util.FloatMath;

import com.example.dam.uebung3.MainActivity;
import com.example.dam.uebung3.Util.Utils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by dam on 11/01/16.
 */
public class Record {

    private int id;
    private double mlsLat;
    private double mlsLng;

    private double gpsLat;
    private double gpsLng;
    private float gpsAcc;

    private double distance;
    private Date date;

    public Record()
    {

    }

    public Record(double mlslat, double mlsLng, double gpslat, double gpsLng, float gpsAcc, double distance, Date date)
    {
        this.mlsLat = mlslat;
        this.mlsLng = mlsLng;
        this.gpsLat = gpslat;
        this.gpsLng = gpsLng;
        this.gpsAcc = gpsAcc;
        this.distance = distance;
        this.date = date;
    }

    public Record(int id, double mlslat, double mlsLng, double gpslat, double gpsLng, float gpsAcc, double distance, Date date)
    {
        this.id = id;
        this.mlsLat = mlslat;
        this.mlsLng = mlsLng;
        this.gpsLat = gpslat;
        this.gpsLng = gpsLng;
        this.gpsAcc = gpsAcc;
        this.distance = distance;
        this.date = date;

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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public String serialize() {
        return id + "#" + mlsLat + "#" + mlsLng + "#" + gpsLat + "#" +
                gpsLng + "#" + gpsAcc + "#" + distance + "#" + Utils.FORMATTER.format(date);
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", mlsLat=" + mlsLat +
                ", mlsLng=" + mlsLng +
                ", gpsLat=" + gpsLat +
                ", gpsLng=" + gpsLng +
                ", gpsAcc=" + gpsAcc +
                ", distance=" + distance +
                ", date=" + date +
                '}';
    }

    public static Record newInstanceFromString(String str) {
        String[] s = str.split("#");
        try {
            return new Record(Integer.valueOf(s[0]),
                    Double.valueOf(s[1]),
                    Double.valueOf(s[2]),
                    Double.valueOf(s[3]),
                    Double.valueOf(s[4]),
                    Float.valueOf(s[5]),
                    Double.valueOf(s[6]),
                    Utils.FORMATTER.parse(s[7]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

