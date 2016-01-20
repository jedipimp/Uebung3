package com.example.dam.uebung3.Model;

/**
 * Created by Mat on 20.01.2016.
 */
public enum RadioType {
    GMS("gsm"), WCDMA("wcdma"), LTE("lte");


    private final String name;

    RadioType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
