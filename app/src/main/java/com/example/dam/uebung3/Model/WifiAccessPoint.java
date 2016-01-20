package com.example.dam.uebung3.Model;

/**
 * Created by Mat on 20.01.2016.
 */
public class WifiAccessPoint {
    String macAddress;
    String ssid;
    // number of milliseconds since network was detected
    Long age;
    // WiFi channel, often 1-13
    Integer channel;
    // in MHz
    Integer frequency;
    // in dBm
    Integer signalStrength;
    // in dB
    Integer signalToNoisRatio;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(Integer signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Integer getSignalToNoisRatio() {
        return signalToNoisRatio;
    }

    public void setSignalToNoisRatio(Integer signalToNoisRatio) {
        this.signalToNoisRatio = signalToNoisRatio;
    }

    @Override
    public String toString() {
        return "WifiAccessPoint{" +
                "macAddress='" + macAddress + '\'' +
                ", ssid='" + ssid + '\'' +
                ", age=" + age +
                ", channel=" + channel +
                ", frequency=" + frequency +
                ", signalStrength=" + signalStrength +
                ", signalToNoisRatio=" + signalToNoisRatio +
                '}';
    }
}
