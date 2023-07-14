package com.example.zivsfitnessapp.models;

import java.io.Serializable;

public class DailyOpeningHours implements Serializable {

    public DailyOpeningHours(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public DailyOpeningHours() {

    }

    public int from;
    public int to;
}