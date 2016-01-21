package com.example.dam.uebung3.Model;

/**
 * Created by Mat on 20.01.2016.
 */
public class CellTower {
    String radioType;
    Integer mobileCountryCode;
    Integer mobileNetworkCode;
    Integer locationAreaCode;
    Integer cellId;
    Integer age;
    Integer psc;
    Integer signalStrength;
    Integer timingAdvance;

    public String getRadioType() {
        return radioType;
    }

    public void setRadioType(String radioType) {
        this.radioType = radioType;
    }
    public void setRadioType(RadioType radioType) {
        this.radioType = radioType.toString();
    }

    public Integer getMobileCountryCode() {
        return mobileCountryCode;
    }

    public void setMobileCountryCode(Integer mobileCountryCode) {
        this.mobileCountryCode = mobileCountryCode;
    }

    public Integer getMobileNetworkCode() {
        return mobileNetworkCode;
    }

    public void setMobileNetworkCode(Integer mobileNetworkCode) {
        this.mobileNetworkCode = mobileNetworkCode;
    }

    public Integer getLocationAreaCode() {
        return locationAreaCode;
    }

    public void setLocationAreaCode(Integer locationAreaCode) {
        this.locationAreaCode = locationAreaCode;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getPsc() {
        return psc;
    }

    public void setPsc(Integer psc) {
        if (psc != -1)
            this.psc = psc;
    }

    public Integer getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(Integer signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Integer getTimingAdvance() {
        return timingAdvance;
    }

    public void setTimingAdvance(Integer timingAdvance) {
        this.timingAdvance = timingAdvance;
    }

    @Override
    public String toString() {
        return "CellTower{" +
                "radioType=" + radioType +
                ", mobileCountryCode=" + mobileCountryCode +
                ", mobileNetworkCode=" + mobileNetworkCode +
                ", locationAreaCode=" + locationAreaCode +
                ", cellId=" + cellId +
                ", age=" + age +
                ", psc=" + psc +
                ", signalStrength=" + signalStrength +
                ", timingAdvance=" + timingAdvance +
                '}';
    }
}
