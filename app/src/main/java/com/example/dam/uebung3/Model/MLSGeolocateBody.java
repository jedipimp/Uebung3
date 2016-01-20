package com.example.dam.uebung3.Model;

import java.util.List;

/**
 * Created by Mat on 20.01.2016.
 */
public class MLSGeolocateBody {
    private String carrier;
    private int homeMobileCountryCode;
    private int homeMobileNetworkCode;
    private List<WifiAccessPoint> wifiAccessPoints;
    private List<CellTower> cellTowers;

    public List<WifiAccessPoint> getWifiAccessPoints() {
        return wifiAccessPoints;
    }

    public void setWifiAccessPoints(List<WifiAccessPoint> wifiAccessPoints) {
        this.wifiAccessPoints = wifiAccessPoints;
    }

    public List<CellTower> getCellTowers() {
        return cellTowers;
    }

    public void setCellTowers(List<CellTower> cellTowers) {
        this.cellTowers = cellTowers;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public int getHomeMobileCountryCode() {
        return homeMobileCountryCode;
    }

    public void setHomeMobileCountryCode(int homeMobileCountryCode) {
        this.homeMobileCountryCode = homeMobileCountryCode;
    }

    public int getHomeMobileNetworkCode() {
        return homeMobileNetworkCode;
    }

    public void setHomeMobileNetworkCode(int homeMobileNetworkCode) {
        this.homeMobileNetworkCode = homeMobileNetworkCode;
    }

    @Override
    public String toString() {
        return "MLSGeolocateBody{" +
                "wifiAccessPoints=" + wifiAccessPoints +
                ", cellTowers=" + cellTowers +
                ", carrier='" + carrier + '\'' +
                ", homeMobileCountryCode=" + homeMobileCountryCode +
                ", homeMobileNetworkCode=" + homeMobileNetworkCode +
                '}';
    }
}
