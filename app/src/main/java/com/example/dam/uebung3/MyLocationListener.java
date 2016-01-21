package com.example.dam.uebung3;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by mat on 21.01.16.
 */
public class MyLocationListener implements LocationListener {

    private Location location;

    public MyLocationListener(Location lastKnownLocation) {
        this.location = lastKnownLocation;
    }


    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location getLocation() {
        return location;
    }
}
