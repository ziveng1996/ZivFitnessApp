package com.example.zivsfitnessapp.models;

public class TrainingSlot {

    public TrainingSlot(int hour, boolean isAvailable) {
        this.hour = hour;
        this.isAvailable = isAvailable;
    }

    public TrainingSlot() {

    }

    private int hour;
    private boolean isAvailable;

    public int getHour() {
        return hour;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
