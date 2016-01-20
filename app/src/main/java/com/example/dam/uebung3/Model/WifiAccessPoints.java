package com.example.dam.uebung3.Model;

import java.util.List;

/**
 * Created by Mat on 20.01.2016.
 */
public class WifiAccessPoints {
    List<WifiAccessPoint> wifiAccessPoints;

    public List<WifiAccessPoint> getWifiAccessPoints() {
        return wifiAccessPoints;
    }

    public void setWifiAccessPoints(List<WifiAccessPoint> wifiAccessPoints) {
        this.wifiAccessPoints = wifiAccessPoints;
    }

    @Override
    public String toString() {
        return "WifiAccessPoints{" +
                "wifiAccessPoints=" + wifiAccessPoints +
                '}';
    }
}
