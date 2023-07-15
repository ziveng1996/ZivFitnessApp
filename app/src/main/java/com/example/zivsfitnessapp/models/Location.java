package com.example.zivsfitnessapp.models;

import java.io.Serializable;

public class Location implements Serializable {

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {

    }

    public double latitude;
    public double longitude;
}
